package ONDA.domain.challenge.dto;

import ONDA.domain.challenge.entity.Challenge;
import ONDA.domain.challenge.entity.ProgressStatus;
import ONDA.domain.challenge.entity.ReviewStatus;
import ONDA.domain.member.entity.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class ChallengeRequest {
    private String title;
    private String content;
    private String image;
    private LocalDate startDate;
    private LocalDate endDate;

    public ChallengeRequest(Challenge challenge) {
        this.title = challenge.getTitle();
        this.content = challenge.getContent();
        this.image = challenge.getImage();
        this.startDate = challenge.getStartDate();
        this.endDate = challenge.getEndDate();
    }

    public Challenge toEntity(Member member) {
        Challenge challenge = Challenge.builder()
                .title(title)
                .content(content)
                .image(image)
                .reviewStatus(ReviewStatus.APPROVED)
                .progressStatus(ProgressStatus.ONGOING)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        // 연관관계 설정
        challenge.setAuthor(member);
        return challenge;
    }
}
