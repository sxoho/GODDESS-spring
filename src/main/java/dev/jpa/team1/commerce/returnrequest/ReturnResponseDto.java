package dev.jpa.team1.commerce.returnrequest;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReturnResponseDto {

    private Long id;
    private Long orderDetailId;
    private Long orderId;
    private Long memberId;
    private String memberName;
    private String memberEmail;
    private String productName;
    private String colorName;
    private String variantName;
    private Integer quantity;
    private Integer price;
    private String reasonType;
    private String reason;
    private String status;
    private String filePath;
    private LocalDateTime createdAt;
    private LocalDateTime processDate;
    private Integer refundAmount;

    public static ReturnResponseDto from(ReturnRequest returnRequest) {
        return ReturnResponseDto.builder()
                .id(returnRequest.getId())
                .orderDetailId(returnRequest.getOrderDetail().getId())
                .orderId(returnRequest.getOrderDetail().getOrders().getId())
                .memberId(returnRequest.getMember().getId())
                .memberName(returnRequest.getMember().getName())
                .memberEmail(returnRequest.getMember().getEmail())
                .productName(returnRequest.getOrderDetail().getProductName())
                .colorName(returnRequest.getOrderDetail().getColorName())
                .variantName(returnRequest.getOrderDetail().getVariantName())
                .quantity(returnRequest.getOrderDetail().getQuantity())
                .price(returnRequest.getOrderDetail().getPrice())
                .reasonType(returnRequest.getReasonType())
                .reason(returnRequest.getReason())
                .status(returnRequest.getStatus())
                .filePath(returnRequest.getFilePath())
                .createdAt(returnRequest.getCreatedAt())
                .processDate(returnRequest.getProcessDate())
                .refundAmount(returnRequest.getRefundAmount())
                .build();
    }
}
