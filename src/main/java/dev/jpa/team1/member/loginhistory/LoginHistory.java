package dev.jpa.team1.member.loginhistory;

import dev.jpa.team1.member.member.Member;

import jakarta.persistence.*;

import lombok.*;

import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "login_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class LoginHistory {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "login_history_seq"
    )
    @SequenceGenerator(
            name = "login_history_seq",
            sequenceName = "login_history_seq",
            allocationSize = 1
    )
    @Column(name = "login_history_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            nullable = false
    )
    private Member member;


    @Column(
            nullable = false,
            length = 1
    )
    private String success;


    @Column(length = 50)
    private String ip;


    @Column(length = 255)
    private String device;


    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;


    @Column(name = "logout_date")
    private LocalDateTime logoutDate;


    /**
     * 로그아웃 시간 저장
     */
    public void logout() {

        this.logoutDate =
                LocalDateTime.now();
    }
}