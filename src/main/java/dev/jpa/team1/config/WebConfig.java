package dev.jpa.team1.config;

import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig
        implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry
    ) {


        /**
         * 상품 이미지
         */
        registry
                .addResourceHandler(
                        "/images/product/**"
                )
                .addResourceLocations(
                        "file:uploads/product/"
                );


        /**
         * 회원 사진
         */
        registry
                .addResourceHandler(
                        "/images/member/**"
                )
                .addResourceLocations(
                        "file:uploads/member/"
                );
    }
}