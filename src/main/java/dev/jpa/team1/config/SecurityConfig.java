package dev.jpa.team1.config;

import dev.jpa.team1.security.JwtTokenProvider;
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
        return new BCryptPasswordEncoder(); // Bcrypt 해싱 알고리즘 사용
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                // CORS 설정 적용
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

//                // 인증 없이 열어둘 엔드포인트
//                .requestMatchers(
//                        "/api/auth/**",
//
//                        "/product/**",
//                        "/category/**",
//                        "/product_color/**",
//                        "/product_color_info/**",
//                        "/product_color_image/**",
//
//                        "/images/**",
//                        "/product_size/**",
//                        "/product_option/**",
//                        "/wishlist/**",
//
//                        "/swagger-ui/**",
//                        "/v3/api-docs/**",
//                        "/swagger-ui.html"
//                ).permitAll()
//
//                // 회원가입 / 로그인 / 중복 검사
//                .requestMatchers(
//                        "/member/save",
//                        "/member/login",
//                        "/member/check_id",
//                        "/member/check_email",
//                        "/member/check_nickname"
//                ).permitAll()
//
//                // 관리자 전용 엔드포인트
//                .requestMatchers("/api/admin/**")
//                        .hasRole("ADMIN")

                        // 그 외 요청은 인증 필요
//                .anyRequest()
//                        .authenticated()

                        // 테스트 중 모든 요청 허용
                        .anyRequest().permitAll()
                );

        // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞단에 추가
//                .addFilterBefore(
//                        new JwtAuthenticationFilter(jwtTokenProvider),
//                        UsernamePasswordAuthenticationFilter.class
//                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of(
                        // localhost 개발 / 배포 테스트
                        "http://localhost:5173",
                        "http://localhost:5174",
                        "http://localhost:3000",
                        "http://localhost:8501",

                        // 팀원 React 개발 서버
                        "http://10.1.205.116:5173",
                        "http://10.1.205.116:5174",
                        "http://10.1.205.105:5174",
                        "http://10.1.205.105:5173",
                        "http://10.1.205.101:5174",
                        "http://10.1.205.101:5173",
                        
                        
                        "http://1.201.122.194:5173",

                        // 팀원 React 배포 테스트 서버
                        "http://10.1.205.116:3000",
                        "http://10.1.205.105:3000",
                        "http://10.1.205.101:3000"
                )
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

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}