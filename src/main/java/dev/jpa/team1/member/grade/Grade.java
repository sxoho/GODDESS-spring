package dev.jpa.team1.member.grade;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "grade")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grade_seq")
    @SequenceGenerator(name = "grade_seq", sequenceName = "grade_seq", allocationSize = 1)
    @Column(name = "grade_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false, length = 1)
    private String visible;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
