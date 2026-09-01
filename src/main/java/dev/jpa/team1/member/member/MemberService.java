package dev.jpa.team1.member.member;

import dev.jpa.team1.member.grade.Grade;
import dev.jpa.team1.member.grade.GradeRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository repository;

    private final GradeRepository gradeRepository;


    /**
     * 관리자 회원 목록
     *
     * ACTIVE 회원만 조회
     */
    @Transactional(readOnly = true)
    public List<MemberResponseDto> findAll() {

        return repository
                .findByStatusOrderByCreatedAtDesc(
                        "ACTIVE"
                )
                .stream()
                .map(MemberResponseDto::from)
                .toList();
    }


    /**
     * 탈퇴 회원 목록 조회
     *
     * INACTIVE
     * UNACTIVE
     * WITHDRAWN
     */
    @Transactional(readOnly = true)
    public List<MemberResponseDto> findWithdrawn() {

        return repository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .filter(member -> {

                    String status =
                            member.getStatus();

                    if (status == null) {

                        return false;
                    }

                    return status.equalsIgnoreCase(
                            "INACTIVE"
                    )
                            ||
                            status.equalsIgnoreCase(
                                    "UNACTIVE"
                            )
                            ||
                            status.equalsIgnoreCase(
                                    "WITHDRAWN"
                            );
                })
                .map(MemberResponseDto::from)
                .toList();
    }


    /**
     * 회원 상세 조회
     */
    @Transactional(readOnly = true)
    public MemberResponseDto findById(
            Long id
    ) {

        Member member =
                repository
                        .findById(
                                id
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "회원 정보를 찾을 수 없습니다. id="
                                                + id
                                )
                        );

        return MemberResponseDto.from(
                member
        );
    }


    /**
     * 아이디 중복 확인
     *
     * true = 중복
     */
    @Transactional(readOnly = true)
    public boolean checkId(
            String loginId
    ) {

        return repository
                .existsByLoginId(
                        loginId
                );
    }


    /**
     * 닉네임 중복 확인
     */
    @Transactional(readOnly = true)
    public boolean checkNickname(
            String nickname
    ) {

        return repository
                .existsByNickname(
                        nickname
                );
    }


    /**
     * 이메일 중복 확인
     */
    @Transactional(readOnly = true)
    public boolean checkEmail(
            String email
    ) {

        return repository
                .existsByEmail(
                        email
                );
    }


    /**
     * 수정용 아이디 중복 확인
     *
     * 현재 회원 제외
     */
    @Transactional(readOnly = true)
    public boolean checkLoginIdUpdate(
            Long memberId,
            String loginId
    ) {

        return repository
                .existsByLoginIdAndIdNot(
                        loginId,
                        memberId
                );
    }


    /**
     * 관리자 수정용
     * 닉네임 중복 확인
     */
    @Transactional(readOnly = true)
    public boolean checkNicknameUpdate(
            Long memberId,
            String nickname
    ) {

        return repository
                .existsByNicknameAndIdNot(
                        nickname,
                        memberId
                );
    }


    /**
     * 관리자 수정용
     * 이메일 중복 확인
     */
    @Transactional(readOnly = true)
    public boolean checkEmailUpdate(
            Long memberId,
            String email
    ) {

        return repository
                .existsByEmailAndIdNot(
                        email,
                        memberId
                );
    }


    /**
     * 회원가입
     */
    @Transactional
    public MemberResponseDto save(
            MemberRequestDto requestDto
    ) {

        if (
                repository.existsByLoginId(
                        requestDto.getLoginId()
                )
        ) {

            throw new IllegalArgumentException(
                    "이미 사용 중인 아이디입니다."
            );
        }


        if (
                repository.existsByNickname(
                        requestDto.getNickname()
                )
        ) {

            throw new IllegalArgumentException(
                    "이미 사용 중인 닉네임입니다."
            );
        }


        if (
                repository.existsByEmail(
                        requestDto.getEmail()
                )
        ) {

            throw new IllegalArgumentException(
                    "이미 사용 중인 이메일입니다."
            );
        }


        /**
         * 1 = USER
         */
        Grade defaultGrade =
                gradeRepository
                        .findById(
                                1L
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "기본 회원 등급이 없습니다."
                                )
                        );


        Member member =
                Member.builder()
                        .grade(
                                defaultGrade
                        )
                        .loginId(
                                requestDto.getLoginId()
                        )
                        .password(
                                requestDto.getPassword()
                        )
                        .name(
                                requestDto.getName()
                        )
                        .nickname(
                                requestDto.getNickname()
                        )
                        .email(
                                requestDto.getEmail()
                        )
                        .tel(
                                formatTel(
                                        requestDto.getTel()
                                )
                        )
                        .zipcode(
                                requestDto.getZipcode()
                        )
                        .address1(
                                requestDto.getAddress1()
                        )
                        .address2(
                                requestDto.getAddress2()
                        )
                        .status(
                                "ACTIVE"
                        )
                        .build();


        Member saved =
                repository.save(
                        member
                );


        return MemberResponseDto.from(
                saved
        );
    }


    /**
     * 관리자 회원 수정
     */
    @Transactional
    public MemberResponseDto update(
            Long memberId,
            MemberUpdateRequestDto requestDto
    ) {

        Member member =
                repository
                        .findById(
                                memberId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "회원 정보를 찾을 수 없습니다. id="
                                                + memberId
                                )
                        );


        /**
         * 닉네임 중복 확인
         */
        if (
                repository
                        .existsByNicknameAndIdNot(
                                requestDto.getNickname(),
                                memberId
                        )
        ) {

            throw new IllegalArgumentException(
                    "이미 사용 중인 닉네임입니다."
            );
        }


        /**
         * 이메일 중복 확인
         */
        if (
                repository
                        .existsByEmailAndIdNot(
                                requestDto.getEmail(),
                                memberId
                        )
        ) {

            throw new IllegalArgumentException(
                    "이미 사용 중인 이메일입니다."
            );
        }


        Grade grade =
                gradeRepository
                        .findById(
                                requestDto.getGradeId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "회원 등급을 찾을 수 없습니다."
                                )
                        );


        member.adminUpdate(
                requestDto.getNickname(),
                requestDto.getPassword(),
                requestDto.getEmail(),
                grade,
                requestDto.getZipcode(),
                requestDto.getAddress1(),
                requestDto.getAddress2()
        );


        return MemberResponseDto.from(
                member
        );
    }


    /**
     * 관리자 회원 삭제 처리
     *
     * 실제 DB DELETE X
     *
     * STATUS = WITHDRAWN
     * WITHDRAWN_AT = 현재 시간
     */
    @Transactional
    public void delete(
            Long memberId
    ) {

        Member member =
                repository
                        .findById(
                                memberId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "회원 정보를 찾을 수 없습니다. id="
                                                + memberId
                                )
                        );


        member.withdraw();
    }
    
    /**
     * 아이디 찾기
     *
     * 이름 + 이메일이 일치하는 회원의 아이디 반환
     */
    @Transactional(readOnly = true)
    public String findLoginId(
            String name,
            String email
    ) {

        Member member =
                repository
                        .findByNameAndEmail(
                                name,
                                email
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "일치하는 회원 정보를 찾을 수 없습니다."
                                )
                        );

        return member.getLoginId();
    }
    
    /**
     * 비밀번호 재설정
     *
     * 아이디 + 이메일이 일치하는 회원을 확인한 후
     * 새로운 비밀번호로 변경
     */
    @Transactional
    public void resetPassword(
            MemberPasswordResetRequestDto requestDto
    ) {

        String loginId =
                requestDto.getLoginId();

        String email =
                requestDto.getEmail();

        String newPassword =
                requestDto.getNewPassword();


        /**
         * 필수값 확인
         */
        if (
            loginId == null ||
            loginId.isBlank()
        ) {
            throw new IllegalArgumentException(
                    "아이디를 입력해주세요."
            );
        }


        if (
            email == null ||
            email.isBlank()
        ) {
            throw new IllegalArgumentException(
                    "이메일을 입력해주세요."
            );
        }


        if (
            newPassword == null ||
            newPassword.isBlank()
        ) {
            throw new IllegalArgumentException(
                    "새 비밀번호를 입력해주세요."
            );
        }


        /**
         * 아이디 + 이메일로 회원 조회
         */
        Member member =
                repository
                        .findByLoginIdAndEmail(
                                loginId,
                                email
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "일치하는 회원 정보를 찾을 수 없습니다."
                                )
                        );


        /**
         * 비밀번호 변경
         */
        member.passwordUpdate(
                newPassword
        );
    }
    
    /**
     * 전화번호 형식 변환
     *
     * 01011112222
     * -> 010-1111-2222
     *
     * 010-1111-2222
     * -> 010-1111-2222
     */
    private String formatTel(
            String tel
    ) {

        if (
                tel == null ||
                tel.isBlank()
        ) {

            return tel;
        }


        /**
         * 하이픈, 공백 등을 제거하고
         * 숫자만 남김
         */
        String number =
                tel.replaceAll(
                        "[^0-9]",
                        ""
                );


        /**
         * 휴대폰 번호 11자리
         *
         * 01011112222
         * -> 010-1111-2222
         */
        if (
                number.length() == 11
        ) {

            return number.substring(
                    0,
                    3
            )
                    + "-"
                    + number.substring(
                            3,
                            7
                    )
                    + "-"
                    + number.substring(
                            7
                    );
        }


        /**
         * 10자리 번호
         *
         * 예:
         * 0101234567
         * -> 010-123-4567
         */
        if (
                number.length() == 10
        ) {

            return number.substring(
                    0,
                    3
            )
                    + "-"
                    + number.substring(
                            3,
                            6
                    )
                    + "-"
                    + number.substring(
                            6
                    );
        }


        /**
         * 예상하지 못한 형식은
         * 기존 입력값 반환
         */
        return tel;
    }
    
}