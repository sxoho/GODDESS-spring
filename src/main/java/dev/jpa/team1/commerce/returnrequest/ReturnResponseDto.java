package dev.jpa.team1.commerce.returnrequest;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReturnResponseDto {

    private Long id;

    private Long orderDetailId;

    private Long memberId;

    private String reasonType;

    private String reason;

    private String status;

    private String filePath;

    private LocalDateTime createdAt;

    private LocalDateTime processDate;

    private Integer refundAmount;

    public static ReturnResponseDto from(ReturnRequest returnRequest) {
        return new ReturnResponseDto(
                returnRequest.getId(),
                returnRequest.getOrderDetail().getId(),
                returnRequest.getMember().getId(),
                returnRequest.getReasonType(),
                returnRequest.getReason(),
                returnRequest.getStatus(),
                returnRequest.getFilePath(),
                returnRequest.getCreatedAt(),
                returnRequest.getProcessDate(),
                returnRequest.getRefundAmount()
        );
    }

}
