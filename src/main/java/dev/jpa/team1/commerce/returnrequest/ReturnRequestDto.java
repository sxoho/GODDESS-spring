package dev.jpa.team1.commerce.returnrequest;

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
public class ReturnRequestDto {
    @NotNull
    private Long orderDetailId;

    @NotNull
    private Long memberId;

    @NotBlank
    @Size(max = 50)
    private String reasonType;

    private String reason;

    @NotBlank
    @Size(max = 50)
    private String status;

    @Size(max = 255)
    private String filePath;
}
