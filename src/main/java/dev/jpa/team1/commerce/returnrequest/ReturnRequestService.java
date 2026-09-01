package dev.jpa.team1.commerce.returnrequest;

import dev.jpa.team1.commerce.orderdetail.OrderDetail;
import dev.jpa.team1.commerce.orderdetail.OrderDetailRepository;
import dev.jpa.team1.exception.CustomException;
import dev.jpa.team1.exception.ErrorCode;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReturnRequestService {

    private final ReturnRequestRepository returnRequestRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final MemberRepository memberRepository;

    // 반품/환불 신청 (사용자)
    @Transactional
    public ReturnResponseDto createReturnRequest(String loginId, ReturnCreateRequestDto requestDto) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        OrderDetail orderDetail = orderDetailRepository.findById(requestDto.getOrderDetailId())
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_DETAIL_NOT_FOUND));

        // 본인의 주문 상품인지 권한 체크
        if (!orderDetail.getOrders().getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "해당 주문 상품에 대한 반품 신청 권한이 없습니다.");
        }

        // 중복 반품 신청 방지 (이미 해당 orderDetailId로 신청된 내역이 있는지 검증)
        if (returnRequestRepository.existsByOrderDetailId(orderDetail.getId())) {
            throw new CustomException(ErrorCode.INVALID_RETURN_STATE, "이미 반품 신청이 접수된 상품입니다.");
        }

        ReturnRequest returnRequest = ReturnRequest.builder()
                .orderDetail(orderDetail)
                .member(member)
                .reasonType(requestDto.getReasonType())
                .reason(requestDto.getReason())
                .status("PENDING") // 접수 대기 상태
                .filePath(requestDto.getFilePath())
                .build();

        ReturnRequest savedReturn = returnRequestRepository.save(returnRequest);

        log.info("반품 신청 완료 - Return ID: {}, Member: {}", savedReturn.getId(), loginId);
        return ReturnResponseDto.from(savedReturn);
    }

    // 내 반품 내역 조회 (사용자)
    @Transactional(readOnly = true)
    public List<ReturnResponseDto> getMyReturnRequests(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<ReturnRequest> returns = returnRequestRepository.findByMemberIdOrderByIdDesc(member.getId());
        return returns.stream()
                .map(ReturnResponseDto::from)
                .collect(Collectors.toList());
    }

    // 반품/환불 처리 확정 (관리자)
    @Transactional
    public ReturnResponseDto processReturnRequest(Long returnId, ReturnProcessRequestDto requestDto) {
        ReturnRequest returnRequest = returnRequestRepository.findById(returnId)
                .orElseThrow(() -> new CustomException(ErrorCode.RETURN_REQUEST_NOT_FOUND));

        if (!"PENDING".equals(returnRequest.getStatus())) {
            throw new CustomException(ErrorCode.INVALID_RETURN_STATE, "이미 처리가 완료되었거나 취소된 반품건입니다.");
        }

        // 비즈니스 메서드(엔티티 내부 로직)를 통해 상태, 환불 금액, 처리일자 갱신
        returnRequest.updateProcess(requestDto.getStatus(), requestDto.getRefundAmount(), LocalDateTime.now());

        log.info("반품 처리 완료 - Return ID: {}, Status: {}, Refund Amount: {}", returnId, requestDto.getStatus(), requestDto.getRefundAmount());
        return ReturnResponseDto.from(returnRequest);
    }
}