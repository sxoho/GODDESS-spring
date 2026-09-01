package dev.jpa.team1.commerce.returnrequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReturnCreateRequestDto {

    @NotNull(message = "반품할 주문 상세(상품) 번호는 필수입니다.")
    private Long orderDetailId;

    @NotBlank(message = "반품 사유 구분(단순변심, 파손 등)을 선택해주세요.")
    private String reasonType;

    private String reason; // 상세 사유 (선택)
    
    private String filePath; // 첨부 이미지 경로 (선택)
}