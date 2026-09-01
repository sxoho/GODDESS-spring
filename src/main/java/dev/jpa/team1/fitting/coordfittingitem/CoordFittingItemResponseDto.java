package dev.jpa.team1.fitting.coordfittingitem;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoordFittingItemResponseDto {
    private Long id;

    private Long coordFittingId;

    private Long productColorId;

    private Long productOptionId;

    private String itemType;

    private Integer sortOrder;


    public static CoordFittingItemResponseDto from(CoordFittingItem item) {
        return new CoordFittingItemResponseDto(
                item.getId(),
                item.getCoordFitting().getId(),
                item.getProductColor() != null ? item.getProductColor().getId() : null,
                item.getProductOption() != null ? item.getProductOption().getId() : null,
                item.getItemType(),
                item.getSortOrder()
        );
    }

}
