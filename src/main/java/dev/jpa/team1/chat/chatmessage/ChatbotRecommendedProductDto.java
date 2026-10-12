package dev.jpa.team1.chat.chatmessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotRecommendedProductDto {

    private Long productId;

    private Long productColorId;

    private String productName;

    private String brand;

    private Integer price;

    private Integer discount;

    private String colorName;

    private String colorCode;

    private String imageUrl;
}