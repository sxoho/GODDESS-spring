package dev.jpa.team1.member.loginhistory;

import dev.jpa.team1.member.member.Member;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class LoginHistoryService {

    private final LoginHistoryRepository repository;


    /**
     * 로그인 내역 저장
     */
    @Transactional
    public LoginHistory save(
            Member member,
            String success,
            String ip,
            String device
    ) {

        LoginHistory history =
                LoginHistory.builder()
                        .member(member)
                        .success(success)
                        .ip(ip)
                        .device(device)
                        .build();


        return repository.save(
                history
        );
    }


    /**
     * 특정 회원 로그인 내역 조회
     *
     * 최신 로그인 순
     */
    @Transactional(readOnly = true)
    public List<LoginHistoryResponseDto>
    findByMemberId(
            Long memberId
    ) {

        return repository
                .findByMemberIdOrderByCreatedAtDesc(
                        memberId
                )
                .stream()
                .map(
                        LoginHistoryResponseDto::from
                )
                .toList();
    }


    /**
     * 로그아웃 시간 저장
     *
     * 해당 회원의
     * 가장 최근 성공 로그인 중
     * logoutDate가 없는 이력에
     * 현재 시간을 저장한다.
     */
    @Transactional
    public void logout(
            Long memberId
    ) {

        LoginHistory history =
                repository
                        .findFirstByMemberIdAndSuccessAndLogoutDateIsNullOrderByCreatedAtDesc(
                                memberId,
                                "Y"
                        )
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "로그인 이력을 찾을 수 없습니다."
                                        )
                        );


        history.logout();
    }
}