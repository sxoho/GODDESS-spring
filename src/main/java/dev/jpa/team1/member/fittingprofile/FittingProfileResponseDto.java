package dev.jpa.team1.member.fittingprofile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;


/**
 * 피팅 프로필 응답 DTO
 */
@Getter
@Builder
public class FittingProfileResponseDto {

    private Long fittingProfileId;

    private Long memberId;

    private BigDecimal height;

    private String bodyType;

    private String topSize;

    private String bottomSize;

    private String preferredFit;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    /**
     * Entity -> DTO 변환
     */
    public static FittingProfileResponseDto from(
        FittingProfile fittingProfile
    ) {

        return FittingProfileResponseDto
            .builder()

            .fittingProfileId(
                fittingProfile.getId()
            )

            .memberId(
                fittingProfile
                    .getMember()
                    .getId()
            )

            .height(
                fittingProfile.getHeight()
            )

            .bodyType(
                fittingProfile.getBodyType()
            )

            .topSize(
                fittingProfile.getTopSize()
            )

            .bottomSize(
                fittingProfile.getBottomSize()
            )

            .preferredFit(
                fittingProfile.getPreferredFit()
            )

            .createdAt(
                fittingProfile.getCreatedAt()
            )

            .updatedAt(
                fittingProfile.getUpdatedAt()
            )

            .build();
    }
}