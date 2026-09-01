package dev.jpa.team1.security;

import dev.jpa.team1.member.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public class CustomUserDetails
        implements UserDetails {

    private final Member member;

    public Member getMember() {
        return member;
    }

    @Override
    public Collection<? extends GrantedAuthority>
    getAuthorities() {

        String roleName =
                "ROLE_USER";

        if (
                member.getGrade() != null &&
                member.getGrade().getId() != null
        ) {

            Long gradeId =
                    member.getGrade().getId();

            /**
             * GRADE_ID
             * 1 ~ 3 = ADMIN
             */
            if (
                    gradeId >= 1 &&
                    gradeId <= 3
            ) {
                roleName =
                        "ROLE_ADMIN";
            }
        }

        return Collections.singletonList(
                new SimpleGrantedAuthority(
                        roleName
                )
        );
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !"WITHDRAWN"
                .equalsIgnoreCase(
                        member.getStatus()
                );
    }
}