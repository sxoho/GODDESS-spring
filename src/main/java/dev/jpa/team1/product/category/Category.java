package dev.jpa.team1.product.category;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @SequenceGenerator(name = "category_seq", sequenceName = "category_seq", allocationSize = 1)
    @Column(name = "category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @Builder.Default
    private List<Category> children = new ArrayList<>();

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Integer sortOrder;

    @Column(nullable = false, length = 1)
    private String visible;

    public void update(
            Category parent,
            String name,
            String description,
            Integer sortOrder,
            String visible
    ) {
        this.parent = parent;
        this.name = name;
        this.description = description;
        this.sortOrder = sortOrder;
        this.visible = visible;
    }

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
