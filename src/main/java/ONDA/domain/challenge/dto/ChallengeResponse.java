package ONDA.domain.challenge.dto;

import ONDA.domain.challenge.entity.Challenge;
import ONDA.domain.challenge.entity.ChallengeCategory;
import ONDA.domain.challenge.entity.ChallengeImage;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChallengeResponse {
    @Schema(description = "챌린지 ID", example = "1")
    private Long challengeId;

    @Schema(description = "작성자", example = "홍길동")
    private String author;

    @Schema(description = "챌린지 제목", example = "영어 7일 챌린지")
    private String title;

    @Schema(description = "챌린지 내용", example = "하루에 영어 단어 10개씩 외우기")
    private String content;

    @Schema(description = "이미지", example = "[https://example.com/image1.png,https://example.com/image2.png]")
    private List<String> images = new ArrayList<>();

    @Schema(description = "심사 상태", example = "PENDING/APPROVED/REJECTED")
    private String reviewStatus;

    @Schema(description = "진행 상태", example = "NOT_STARTED/ONGOING/ENDED")
    private String progressStatus;

    @Schema(description = "챌린지 시작일", example = "2025-09-01")
    private LocalDate startDate;

    @Schema(description = "챌린지 종료일", example = "2025-09-08")
    private LocalDate endDate;

    @Schema(description = "작성일자", example = "2025-08-30 15:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "챌린지 카테고리", example = "외국어 · 번역 · 통역")
    private List<String> challengeCategories = new ArrayList<>();

    public ChallengeResponse(Challenge challenge) {
        this.author = challenge.getAuthor().getNickname();
        this.title = challenge.getTitle();
        this.content = challenge.getContent();
        this.reviewStatus = challenge.getReviewStatus().getDisplayName();
        this.progressStatus = challenge.getProgressStatus().getDisplayName();
        this.startDate = challenge.getStartDate();
        this.endDate = challenge.getEndDate();
        this.createdAt = challenge.getCreatedAt();

        List<ChallengeImage> postImages = challenge.getImages();
        for(ChallengeImage challengeImage : postImages){
            images.add(challengeImage.getUrl());
        }

        List<ChallengeCategory> categories = challenge.getCategories();
        for(ChallengeCategory challengeCategory : categories){
            challengeCategories.add(challengeCategory.getCategory().getName());
        }
    }
}
