package dev.jpa.team1.commerce.returnrequest;

import dev.jpa.team1.commerce.orderdetail.OrderDetail;
import dev.jpa.team1.commerce.orderdetail.OrderDetailRepository;
import dev.jpa.team1.exception.CustomException;
import dev.jpa.team1.exception.ErrorCode;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReturnRequestService {

    private final ReturnRequestRepository returnRequestRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final MemberRepository memberRepository;

    /**
     * 반품 요청 등록
     * - 로그인 회원 본인 주문인지 확인
     * - 배송완료된 상품만 허용
     * - 주문상세 상품 1건당 반품 요청 1건만 허용
     */
    @Transactional
    public ReturnResponseDto createReturnRequest(
            String loginId,
            ReturnCreateRequestDto requestDto
    ) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        OrderDetail orderDetail = orderDetailRepository.findById(requestDto.getOrderDetailId())
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_DETAIL_NOT_FOUND));

        if (!orderDetail.getOrders().getMember().getId().equals(member.getId())) {
            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "해당 주문 상품에 대한 반품 요청 권한이 없습니다."
            );
        }

        if (!"DELIVERED".equals(orderDetail.getOrders().getStatus())) {
            throw new CustomException(
                    ErrorCode.INVALID_RETURN_STATE,
                    "배송완료된 상품만 반품 요청할 수 있습니다."
            );
        }

        if (returnRequestRepository.existsByOrderDetailId(orderDetail.getId())) {
            throw new CustomException(
                    ErrorCode.INVALID_RETURN_STATE,
                    "이미 반품 요청이 접수된 상품입니다."
            );
        }

        ReturnRequest returnRequest = ReturnRequest.builder()
                .orderDetail(orderDetail)
                .member(member)
                .reasonType(requestDto.getReasonType().trim().toUpperCase())
                .reason(requestDto.getReason())
                .status("PENDING")
                .filePath(requestDto.getFilePath())
                .build();

        ReturnRequest savedReturn = returnRequestRepository.save(returnRequest);

        log.info(
                "반품 요청 완료 - Return ID: {}, Member: {}, OrderDetail: {}",
                savedReturn.getId(),
                loginId,
                orderDetail.getId()
        );

        return ReturnResponseDto.from(savedReturn);
    }

    /** 내 반품 요청 조회 */
    @Transactional(readOnly = true)
    public List<ReturnResponseDto> getMyReturnRequests(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return returnRequestRepository
                .findByMemberIdOrderByIdDesc(member.getId())
                .stream()
                .map(ReturnResponseDto::from)
                .toList();
    }

    /** 관리자 반품 요청 전체 조회 */
    @Transactional(readOnly = true)
    public List<ReturnResponseDto> getAdminReturnRequests() {
        return returnRequestRepository
                .findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(ReturnResponseDto::from)
                .toList();
    }

    /**
     * 관리자 승인 / 거절
     * 메일은 자동 발송하지 않는다.
     * 처리 후 React 관리자 화면에서 메일 작성 화면으로 이동해 직접 발송한다.
     */
    @Transactional
    public ReturnResponseDto processReturnRequest(
            Long returnId,
            ReturnProcessRequestDto requestDto
    ) {
        ReturnRequest returnRequest = returnRequestRepository.findById(returnId)
                .orElseThrow(() -> new CustomException(ErrorCode.RETURN_REQUEST_NOT_FOUND));

        if (!"PENDING".equals(returnRequest.getStatus())) {
            throw new CustomException(
                    ErrorCode.INVALID_RETURN_STATE,
                    "이미 처리된 반품 요청입니다."
            );
        }

        String status = requestDto.getStatus().trim().toUpperCase();

        if (!"APPROVED".equals(status) && !"REJECTED".equals(status)) {
            throw new CustomException(
                    ErrorCode.INVALID_INPUT_VALUE,
                    "반품 요청은 APPROVED 또는 REJECTED 상태로만 처리할 수 있습니다."
            );
        }

        int refundAmount = requestDto.getRefundAmount() == null
                ? 0
                : Math.max(0, requestDto.getRefundAmount());

        if ("REJECTED".equals(status)) {
            refundAmount = 0;
        }

        returnRequest.updateProcess(
                status,
                refundAmount,
                LocalDateTime.now()
        );

        log.info(
                "반품 요청 처리 - Return ID: {}, Status: {}, Refund Amount: {}",
                returnId,
                status,
                refundAmount
        );

        return ReturnResponseDto.from(returnRequest);
    }
}
