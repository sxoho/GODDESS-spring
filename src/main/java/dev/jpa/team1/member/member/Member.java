package dev.jpa.team1.member.member;

import dev.jpa.team1.member.grade.Grade;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "member_seq"
    )
    @SequenceGenerator(
            name = "member_seq",
            sequenceName = "member_seq",
            allocationSize = 1
    )
    @Column(name = "member_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    private Grade grade;

    @Column(
            nullable = false,
            unique = true,
            length = 50
    )
    private String loginId;

    @Column(
            nullable = false,
            length = 255
    )
    private String password;

    @Column(
            nullable = false,
            length = 50
    )
    private String name;

    @Column(
            nullable = false,
            unique = true,
            length = 50
    )
    private String nickname;

    @Column(
            nullable = false,
            unique = true,
            length = 100
    )
    private String email;

    @Column(length = 20)
    private String tel;

    @Column(length = 10)
    private String zipcode;

    @Column(length = 255)
    private String address1;

    @Column(length = 255)
    private String address2;

    @Column(
            nullable = false,
            length = 20
    )
    private String status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "withdrawn_at")
    private LocalDateTime withdrawnAt;

    /**
     * 관리자 회원 정보 수정
     */
    public void adminUpdate(
            String nickname,
            String password,
            String email,
            Grade grade,
            String zipcode,
            String address1,
            String address2
    ) {

        this.nickname = nickname;

        /**
         * 비밀번호를 입력한 경우에만 변경
         * 빈 문자열이면 기존 비밀번호 유지
         */
        if (
                password != null &&
                !password.isBlank()
        ) {
            this.password = password;
        }

        this.email = email;
        this.grade = grade;
        this.zipcode = zipcode;
        this.address1 = address1;
        this.address2 = address2;
    }
    
    /**
     * 마이페이지 회원 정보 수정
     *
     * 수정 가능:
     * 아이디
     * 닉네임
     * 우편번호
     * 기본 주소
     * 상세 주소
     */
    public void myPageUpdate(
            String loginId,
            String nickname,
            String zipcode,
            String address1,
            String address2
    ) {

        this.loginId =
                loginId;

        this.nickname =
                nickname;

        this.zipcode =
                zipcode;

        this.address1 =
                address1;

        this.address2 =
                address2;

        this.updatedAt =
                LocalDateTime.now();
    }

    /**
     * 회원 탈퇴 처리
     *
     * 실제 DELETE하지 않고
     * 회원 상태만 WITHDRAWN으로 변경
     */
    public void withdraw() {

        this.status = "WITHDRAWN";

        this.withdrawnAt =
                LocalDateTime.now();
    }
    
    /**
     * 비밀번호 변경
     *
     * @param password 새로운 비밀번호
     */
    public void passwordUpdate(
            String password
    ) {
        this.password = password;
    }
}