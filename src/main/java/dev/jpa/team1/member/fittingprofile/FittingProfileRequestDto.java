package dev.jpa.team1.member.fittingprofile;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 피팅 프로필 등록 / 수정 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class FittingProfileRequestDto {

    /**
     * 회원 번호
     */
    @NotNull(
        message = "회원 번호는 필수입니다."
    )
    private Long memberId;


    /**
     * 키
     */
    @NotNull(
        message = "키를 입력해주세요."
    )
    @DecimalMin(
        value = "100.0",
        message = "키는 100cm 이상으로 입력해주세요."
    )
    @DecimalMax(
        value = "250.0",
        message = "키는 250cm 이하로 입력해주세요."
    )
    private BigDecimal height;


    /**
     * 체형
     */
    @NotBlank(
        message = "체형을 선택해주세요."
    )
    @Size(
        max = 50,
        message = "체형 값은 50자 이하로 입력해주세요."
    )
    private String bodyType;


    /**
     * 평소 상의 사이즈
     */
    @NotBlank(
        message = "평소 상의 사이즈를 선택해주세요."
    )
    @Size(
        max = 20,
        message = "상의 사이즈는 20자 이하로 입력해주세요."
    )
    private String topSize;


    /**
     * 평소 하의 사이즈
     */
    @NotBlank(
        message = "평소 하의 사이즈를 선택해주세요."
    )
    @Size(
        max = 20,
        message = "하의 사이즈는 20자 이하로 입력해주세요."
    )
    private String bottomSize;


    /**
     * 선호 핏
     */
    @NotBlank(
        message = "선호 핏을 선택해주세요."
    )
    @Size(
        max = 50,
        message = "선호 핏은 50자 이하로 입력해주세요."
    )
    private String preferredFit;
}