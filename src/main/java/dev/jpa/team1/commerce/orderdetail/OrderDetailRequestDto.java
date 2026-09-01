package dev.jpa.team1.commerce.orderdetail;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailRequestDto {
    @NotNull
    private Long productOptionId;

    private Long quickFittingResultId;

    private Long coordFittingResultId;

    @NotBlank
    @Size(max = 255)
    private String productName;

    @NotBlank
    @Size(max = 50)
    private String colorName;

    @NotBlank
    @Size(max = 50)
    private String sizeName;

    @NotNull
    private Integer price;

    @NotNull
    @Min(1)
    private Integer quantity;
}
