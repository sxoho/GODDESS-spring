package dev.jpa.team1.fitting.virtualfitting;

import dev.jpa.team1.exception.CustomException;
import dev.jpa.team1.exception.ErrorCode;
import dev.jpa.team1.fitting.coordfitting.CoordFitting;
import dev.jpa.team1.fitting.coordfitting.CoordFittingRepository;
import dev.jpa.team1.fitting.coordfitting.CoordFittingRequestDto;
import dev.jpa.team1.fitting.coordfitting.CoordFittingResponseDto;
import dev.jpa.team1.fitting.coordfittingitem.CoordFittingItem;
import dev.jpa.team1.fitting.coordfittingitem.CoordFittingItemDto;
import dev.jpa.team1.fitting.coordfittingitem.CoordFittingItemRepository;
import dev.jpa.team1.fitting.coordfittingresult.CoordFittingResult;
import dev.jpa.team1.fitting.coordfittingresult.CoordFittingResultRepository;
import dev.jpa.team1.fitting.fittingsave.FittingSave;
import dev.jpa.team1.fitting.fittingsave.FittingSaveRepository;
import dev.jpa.team1.fitting.fittingsave.FittingSaveRequestDto;
import dev.jpa.team1.fitting.fittingsave.FittingSaveResponseDto;
import dev.jpa.team1.fitting.quickfitting.QuickFitting;
import dev.jpa.team1.fitting.quickfitting.QuickFittingRepository;
import dev.jpa.team1.fitting.quickfitting.QuickFittingRequestDto;
import dev.jpa.team1.fitting.quickfitting.QuickFittingResponseDto;
import dev.jpa.team1.fitting.quickfittingresult.QuickFittingResult;
import dev.jpa.team1.fitting.quickfittingresult.QuickFittingResultRepository;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;
import dev.jpa.team1.member.memberphoto.MemberPhoto;
import dev.jpa.team1.member.memberphoto.MemberPhotoRepository;
import dev.jpa.team1.product.product.Product;
import dev.jpa.team1.product.product.ProductRepository;
import dev.jpa.team1.product.productcolor.ProductColor;
import dev.jpa.team1.product.productcolor.ProductColorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VirtualFittingService {

    private final MemberRepository memberRepository;
    private final MemberPhotoRepository memberPhotoRepository;
    private final ProductRepository productRepository;
    private final ProductColorRepository productColorRepository;
    private final QuickFittingRepository quickFittingRepository;
    private final QuickFittingResultRepository quickFittingResultRepository;
    private final CoordFittingRepository coordFittingRepository;
    private final CoordFittingItemRepository coordFittingItemRepository;
    private final CoordFittingResultRepository coordFittingResultRepository;
    private final FittingSaveRepository fittingSaveRepository;

    // 1. 간편 피팅 요청 등록 (초기 상태: PENDING)
    @Transactional
    public QuickFittingResponseDto createQuickFitting(String loginId, QuickFittingRequestDto requestDto) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        MemberPhoto photo = memberPhotoRepository.findById(requestDto.getMemberPhotoId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));

        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        QuickFitting quickFitting = QuickFitting.builder()
                .member(member)
                .memberPhoto(photo)
                .product(product)
                .status("PENDING")
                .errorMessage(null)
                .build();

        QuickFitting savedFitting = quickFittingRepository.save(quickFitting);
        log.info("간편 피팅 요청 생성 완료 - ID: {}, Member: {}", savedFitting.getId(), loginId);

        return QuickFittingResponseDto.from(savedFitting, null);
    }

    // 2. [FastAPI Callback] AI 간편 피팅 합성 완료 및 결과 저장 (상태: SUCCESS)
    @Transactional
    public QuickFittingResponseDto completeQuickFittingResult(Long quickFittingId,
                                                              Long productColorId,
                                                              String fileOrigin,
                                                              String fileSaved,
                                                              String filePath,
                                                              String modelFilePath,
                                                              String aiDescription) {
        QuickFitting fitting = quickFittingRepository.findById(quickFittingId)
                .orElseThrow(() -> new CustomException(ErrorCode.FITTING_NOT_FOUND));

        ProductColor color = productColorRepository.findById(productColorId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        // 피팅 결과 엔티티 생성
        QuickFittingResult result = QuickFittingResult.builder()
                .quickFitting(fitting)
                .productColor(color)
                .fileOrigin(fileOrigin)
                .fileSaved(fileSaved)
                .filePath(filePath)
                .modelFilePath(modelFilePath)
                .aiDescription(aiDescription)
                .success("Y")
                .build();

        QuickFittingResult savedResult = quickFittingResultRepository.save(result);

        // 요청 건의 상태를 SUCCESS로 업데이트
        fitting.updateStatus("SUCCESS", null);

        log.info("간편 피팅 AI 결과 저장 완료 - Result ID: {}, Fitting ID: {}", savedResult.getId(), quickFittingId);
        return QuickFittingResponseDto.from(fitting, savedResult);
    }

    // 3. 코디 피팅 요청 및 아이템 구성 등록
    @Transactional
    public CoordFittingResponseDto createCoordFitting(String loginId, CoordFittingRequestDto requestDto) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        MemberPhoto photo = null;
        if (requestDto.getMemberPhotoId() != null) {
            photo = memberPhotoRepository.findById(requestDto.getMemberPhotoId())
                    .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE));
        }

        CoordFitting coordFitting = CoordFitting.builder()
                .member(member)
                .memberPhoto(photo)
                .fittingMethod(requestDto.getFittingMethod())
                .topColorId(requestDto.getTopColorId())
                .bottomColorId(requestDto.getBottomColorId())
                .outerColorId(requestDto.getOuterColorId())
                .status("PENDING")
                .errorMessage(null)
                .build();

        CoordFitting savedCoordFitting = coordFittingRepository.save(coordFitting);

        // 하위 코디 선택 아이템들 리스트 저장
        for (CoordFittingItemDto itemDto : requestDto.getItems()) {
            ProductColor color = productColorRepository.findById(itemDto.getProductColorId())
                    .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

            CoordFittingItem coordItem = CoordFittingItem.builder()
                    .coordFitting(savedCoordFitting)
                    .productColor(color)
                    .itemType(itemDto.getItemType())
                    .sortOrder(itemDto.getSortOrder())
                    .build();

            coordFittingItemRepository.save(coordItem);
        }

        log.info("코디 피팅 요청 생성 완료 - ID: {}, Member: {}", savedCoordFitting.getId(), loginId);
        return CoordFittingResponseDto.from(savedCoordFitting, null);
    }

    // 4. 피팅 비교함/보관함에 저장
    @Transactional
    public FittingSaveResponseDto saveFittingToBox(String loginId, FittingSaveRequestDto requestDto) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        QuickFittingResult quickResult = null;
        CoordFittingResult coordResult = null;

        if ("QUICK".equalsIgnoreCase(requestDto.getFittingType())) {
            if (requestDto.getQuickFittingResultId() == null) {
                throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
            }
            quickResult = quickFittingResultRepository.findById(requestDto.getQuickFittingResultId())
                    .orElseThrow(() -> new CustomException(ErrorCode.FITTING_RESULT_NOT_FOUND));
        } else if ("COORD".equalsIgnoreCase(requestDto.getFittingType())) {
            if (requestDto.getCoordFittingResultId() == null) {
                throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
            }
            coordResult = coordFittingResultRepository.findById(requestDto.getCoordFittingResultId())
                    .orElseThrow(() -> new CustomException(ErrorCode.FITTING_RESULT_NOT_FOUND));
        } else {
            throw new CustomException(ErrorCode.INVALID_FITTING_TYPE);
        }

        FittingSave fittingSave = FittingSave.builder()
                .member(member)
                .fittingType(requestDto.getFittingType().toUpperCase())
                .quickFittingResult(quickResult)
                .coordFittingResult(coordResult)
                .saveName(requestDto.getSaveName())
                .memo(requestDto.getMemo())
                .build();

        FittingSave saved = fittingSaveRepository.save(fittingSave);
        log.info("피팅 결과 보관함 저장 완료 - Save ID: {}, Member: {}", saved.getId(), loginId);

        return FittingSaveResponseDto.from(saved);
    }

    // 5. 사용자의 보관함 피팅 리스트 조회
    @Transactional(readOnly = true)
    public List<FittingSaveResponseDto> getMySavedFittings(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        List<FittingSave> saves = fittingSaveRepository.findAllByMemberIdOrderByIdDesc(member.getId());
        return saves.stream()
                .map(FittingSaveResponseDto::from)
                .collect(Collectors.toList());
    }
}