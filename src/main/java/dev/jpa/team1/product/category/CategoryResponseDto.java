package dev.jpa.team1.product.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {
    private Long id;

    private Long parentId;

    private String name;

    private String description;

    private Integer sortOrder;

    private String visible;

    private List<CategoryResponseDto> children;

    private LocalDateTime createdAt;


    public static CategoryResponseDto from(Category category) {
        List<CategoryResponseDto> childDtos = category.getChildren() != null
                ? category.getChildren().stream().map(CategoryResponseDto::from).toList()
                : List.of();

        return new CategoryResponseDto(
                category.getId(),
                category.getParent() != null ? category.getParent().getId() : null,
                category.getName(),
                category.getDescription(),
                category.getSortOrder(),
                category.getVisible(),
                childDtos,
                category.getCreatedAt()
        );
    }

}
