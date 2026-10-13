package dev.jpa.team1.config;

import dev.jpa.team1.security.JwtAuthenticationFilter;
import dev.jpa.team1.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                .cors(cors ->
                        cors.configurationSource(
                                corsConfigurationSource()
                        )
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                /**
                 * 인증되지 않은 요청은
                 * 403이 아니라 401 반환
                 *
                 * Access Token 만료 시
                 * React에서 Refresh Token 재발급을
                 * 처리할 수 있도록 한다.
                 */
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(
                                (request, response, authException) -> {

                                    response.setStatus(
                                            HttpServletResponse.SC_UNAUTHORIZED
                                    );
                                }
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // .requestMatchers(
                        //         "/api/auth/**"
                        // ).permitAll()

                        .requestMatchers(
                                "/api/commerce/cart/**"
                        ).authenticated()

                        .requestMatchers(
                                "/api/commerce/orders/**"
                        ).authenticated()

                        .requestMatchers(
                                "/api/commerce/payment/**"
                        ).authenticated()

                        .requestMatchers(
                                "/api/commerce/admin/orders/**"
                        ).authenticated()

                        /*
                         * =========================================
                         * CHATBOT
                         * =========================================
                         *
                         * 현재 Swagger 테스트 단계에서는
                         * JWT 인증을 사용하지 않고
                         * memberId를 직접 전달하여 테스트한다.
                         *
                         * React 챗봇 연결 완료 후
                         * 다시 authenticated() 적용 예정.
                         */
                        // .requestMatchers(
                        //         "/api/chat/**"
                        // ).authenticated()

                        .anyRequest().permitAll()
                )

                .addFilterBefore(
                        new JwtAuthenticationFilter(
                                jwtTokenProvider
                        ),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        /*
         * 개발 환경 CORS 전체 허용
         *
         * allowCredentials(true)를 사용하고 있으므로
         * setAllowedOrigins("*") 대신
         * setAllowedOriginPatterns("*")를 사용한다.
         */
        configuration.setAllowedOriginPatterns(
                List.of("*")
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "PATCH",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        configuration.setAllowCredentials(
                true
        );

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}