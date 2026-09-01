package dev.jpa.team1.member.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {

    private Long id;

    private String loginId;

    private String name;

    private String nickname;

    private String email;

    private String tel;

    private String zipcode;

    private String address1;

    private String address2;

    private String status;

    private Long gradeId;

    private String gradeName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime withdrawnAt;

    public static MemberResponseDto from(
            Member member
    ) {

        return new MemberResponseDto(
                member.getId(),
                member.getLoginId(),
                member.getName(),
                member.getNickname(),
                member.getEmail(),
                member.getTel(),
                member.getZipcode(),
                member.getAddress1(),
                member.getAddress2(),
                member.getStatus(),

                member.getGrade() != null
                        ? member.getGrade().getId()
                        : null,

                member.getGrade() != null
                        ? member.getGrade().getName()
                        : null,

                member.getCreatedAt(),
                member.getUpdatedAt(),
                member.getWithdrawnAt()
        );
    }
}