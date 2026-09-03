package dev.jpa.team1.commerce.orderdetail;
import jakarta.validation.constraints.*; import lombok.*;
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class OrderDetailRequestDto {
 @NotNull private Long productOptionId; @NotBlank @Size(max=200) private String productName; @Size(max=50) private String colorName; @Size(max=50) private String variantName; @NotNull @Min(0) private Integer price; @NotNull @Min(1) private Integer quantity;
}
