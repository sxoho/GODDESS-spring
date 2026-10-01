package dev.jpa.team1.beauty.save;


import dev.jpa.team1.beauty.tryon.BeautyTryon;

import dev.jpa.team1.beauty.tryonitem.BeautyTryonItem;
import dev.jpa.team1.beauty.tryonitem.BeautyTryonItemRepository;

import dev.jpa.team1.beauty.tryonresult.BeautyTryonResult;
import dev.jpa.team1.beauty.tryonresult.BeautyTryonResultRepository;

import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BeautySaveService {


    private final BeautySaveRepository beautySaveRepository;

    private final BeautyTryonResultRepository
            beautyTryonResultRepository;

    private final BeautyTryonItemRepository
            beautyTryonItemRepository;

    private final MemberRepository memberRepository;


    /* =========================================
       AI BEAUTY 결과 저장
    ========================================= */

    @Transactional
    public BeautySaveResponseDto saveResult(
            BeautySaveRequestDto requestDto
    ) {


        if (
            requestDto.getMemberId() == null
        ) {

            throw new IllegalArgumentException(
                "회원 ID가 필요합니다."
            );
        }


        if (
            requestDto.getBeautyResultId() == null
        ) {

            throw new IllegalArgumentException(
                "AI BEAUTY 결과 ID가 필요합니다."
            );
        }


        /* 회원 조회 */

        Member member =
                memberRepository
                    .findById(
                        requestDto.getMemberId()
                    )
                    .orElseThrow(
                        () ->
                            new IllegalArgumentException(
                                "회원을 찾을 수 없습니다."
                            )
                    );


        /* 결과 조회 */

        BeautyTryonResult result =
                beautyTryonResultRepository
                    .findById(
                        requestDto.getBeautyResultId()
                    )
                    .orElseThrow(
                        () ->
                            new IllegalArgumentException(
                                "AI BEAUTY 결과를 찾을 수 없습니다."
                            )
                    );


        /* 결과 소유 회원 검증 */

        Long resultMemberId =
                result
                    .getBeautyTryon()
                    .getMember()
                    .getId();


        if (
            !resultMemberId.equals(
                member.getId()
            )
        ) {

            throw new IllegalArgumentException(
                "본인의 AI BEAUTY 결과만 저장할 수 있습니다."
            );
        }


        /* 정상 결과인지 확인 */

        if (
            !"Y".equalsIgnoreCase(
                result.getSuccess()
            )
        ) {

            throw new IllegalArgumentException(
                "정상적으로 생성된 AI BEAUTY 결과만 저장할 수 있습니다."
            );
        }


        /* 중복 저장 확인 */

        boolean alreadySaved =
                beautySaveRepository
                    .existsByMemberIdAndBeautyTryonResultId(
                        member.getId(),
                        result.getId()
                    );


        if (alreadySaved) {

            throw new IllegalArgumentException(
                "이미 저장한 AI BEAUTY 결과입니다."
            );
        }


        String saveName =
                requestDto.getSaveName();


        if (
            saveName == null ||
            saveName.isBlank()
        ) {

            saveName =
                    "AI BEAUTY 결과";
        }


        if (
            saveName.length() > 100
        ) {

            saveName =
                    saveName.substring(
                        0,
                        100
                    );
        }


        String memo =
                requestDto.getMemo();


        if (
            memo != null &&
            memo.length() > 500
        ) {

            memo =
                    memo.substring(
                        0,
                        500
                    );
        }


        BeautySave beautySave =
                BeautySave
                    .builder()
                    .member(
                        member
                    )
                    .beautyTryonResult(
                        result
                    )
                    .saveName(
                        saveName
                    )
                    .memo(
                        memo
                    )
                    .build();


        beautySave =
                beautySaveRepository.save(
                    beautySave
                );


        return BeautySaveResponseDto
                .builder()
                .success(
                    true
                )
                .message(
                    "AI BEAUTY 결과가 저장되었습니다."
                )
                .beautySaveId(
                    beautySave.getId()
                )
                .beautyResultId(
                    result.getId()
                )
                .saveName(
                    beautySave.getSaveName()
                )
                .build();
    }


    /* =========================================
       MY HISTORY 조회
    ========================================= */

    @Transactional(readOnly = true)
    public List<BeautySaveHistoryDto>
    getMemberHistory(
            Long memberId
    ) {


        if (
            memberId == null
        ) {

            throw new IllegalArgumentException(
                "회원 ID가 필요합니다."
            );
        }


        return beautySaveRepository
                .findByMemberIdOrderByCreatedAtDesc(
                    memberId
                )
                .stream()
                .map(
                    this::toHistoryDto
                )
                .toList();
    }


    /* =========================================
       BEAUTY_SAVE → History DTO
    ========================================= */

    private BeautySaveHistoryDto toHistoryDto(
            BeautySave beautySave
    ) {


        BeautyTryonResult result =
                beautySave
                    .getBeautyTryonResult();


        BeautyTryon tryon =
                result
                    .getBeautyTryon();


        List<BeautyTryonItem> tryonItems =
                beautyTryonItemRepository
                    .findByBeautyTryonIdOrderBySortOrderAsc(
                        tryon.getId()
                    );


        List<BeautySaveHistoryDto.ItemDto> itemDtos =
                tryonItems
                    .stream()
                    .map(
                        this::toItemDto
                    )
                    .toList();


        String beforeFilePath =
                tryon.getMemberPhoto() != null
                    ? tryon
                        .getMemberPhoto()
                        .getFilePath()
                    : null;


        return BeautySaveHistoryDto
                .builder()
                .beautySaveId(
                    beautySave.getId()
                )
                .beautyResultId(
                    result.getId()
                )
                .beautyTryonId(
                    tryon.getId()
                )
                .saveName(
                    beautySave.getSaveName()
                )
                .memo(
                    beautySave.getMemo()
                )
                .createdAt(
                    beautySave.getCreatedAt()
                )
                .beautyType(
                    tryon.getBeautyType()
                )
                .tryonStatus(
                    tryon.getStatus()
                )
                .resultFilePath(
                    result.getFilePath()
                )
                .beforeFilePath(
                    beforeFilePath
                )
                .items(
                    itemDtos
                )
                .build();
    }


    /* =========================================
       BEAUTY_TRYON_ITEM → Item DTO
    ========================================= */

    private BeautySaveHistoryDto.ItemDto
    toItemDto(
            BeautyTryonItem item
    ) {


        Long productId =
                null;

        String productName =
                null;

        String brand =
                null;

        Integer price =
                null;


        if (
            item.getProduct() != null
        ) {

            productId =
                    item
                        .getProduct()
                        .getId();

            productName =
                    item
                        .getProduct()
                        .getName();

            brand =
                    item
                        .getProduct()
                        .getBrand();

            price =
                    item
                        .getProduct()
                        .getPrice();
        }


        Long productColorId =
                null;

        String colorName =
                null;

        String colorCode =
                null;


        if (
            item.getProductColor() != null
        ) {

            productColorId =
                    item
                        .getProductColor()
                        .getId();


            if (
                item
                    .getProductColor()
                    .getColorMaster()
                    != null
            ) {

                colorName =
                        item
                            .getProductColor()
                            .getColorMaster()
                            .getColorName();

                colorCode =
                        item
                            .getProductColor()
                            .getColorMaster()
                            .getColorCode();
            }
        }


        return BeautySaveHistoryDto
                .ItemDto
                .builder()
                .itemType(
                    item.getItemType()
                )
                .styleValue(
                    item.getStyleValue()
                )
                .productId(
                    productId
                )
                .productName(
                    productName
                )
                .brand(
                    brand
                )
                .productColorId(
                    productColorId
                )
                .colorName(
                    colorName
                )
                .colorCode(
                    colorCode
                )
                .price(
                    price
                )
                .build();
    }
    
    @Transactional
    public void deleteSavedHistory(
            Long beautySaveId,
            Long memberId
    ) {

        if (
            beautySaveId == null
            ||
            memberId == null
        ) {

            throw new IllegalArgumentException(
                "저장 내역 ID와 회원 ID가 필요합니다."
            );
        }


        BeautySave beautySave =
                beautySaveRepository
                    .findById(
                        beautySaveId
                    )
                    .orElseThrow(
                        () ->
                            new IllegalArgumentException(
                                "저장된 AI BEAUTY 이용 내역을 찾을 수 없습니다."
                            )
                    );


        Long ownerMemberId =
                beautySave
                    .getMember()
                    .getId();


        if (
            !ownerMemberId.equals(
                memberId
            )
        ) {

            throw new IllegalArgumentException(
                "본인의 AI BEAUTY 이용 내역만 삭제할 수 있습니다."
            );
        }


        beautySaveRepository.delete(
            beautySave
        );
    }
}