package dev.jpa.team1.fitting.quickfitting;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class QuickFittingRequestDto {

    @NotNull
    private String memberId;

    @NotNull(message = "회원 사진 ID는 필수 입력값입니다.")
    private Long memberPhotoId;

    @NotNull(message = "상품 ID는 필수 입력값입니다.")
    private Long productId;
}