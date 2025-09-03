package ONDA.domain.challenge.dto;

import ONDA.domain.challenge.entity.Challenge;
import ONDA.domain.challenge.entity.ProgressStatus;
import ONDA.domain.challenge.entity.ReviewStatus;
import ONDA.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ChallengeRequest {
    @Schema(description = "챌린지 제목", example = "영어 7일 챌린지")
    private String title;

    @Schema(description = "챌린지 내용", example = "하루에 영어 단어 10개씩 외우기")
    private String content;

    @Schema(description = "이미지 url", example = "https://example.com/image2.png")
    private String image;

    @Schema(description = "챌린지 시작일", example = "2025-09-01")
    private LocalDate startDate;

    @Schema(description = "챌린지 종료일", example = "2025-09-08")
    private LocalDate endDate;

    public Challenge toEntity(Member member) {
        Challenge challenge = Challenge.builder()
                .title(title)
                .content(content)
                .image(image)
                .reviewStatus(ReviewStatus.APPROVED)
                .progressStatus(ProgressStatus.ONGOING)
                .startDate(startDate)
                .endDate(endDate)
                .createdAt(LocalDateTime.now())
                .build();

        // 연관관계 설정
        challenge.setAuthor(member);
        return challenge;
    }
}
