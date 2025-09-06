package ONDA.domain.challenge.dto;

import ONDA.domain.challenge.entity.Challenge;
import ONDA.domain.challenge.entity.ChallengeCategory;
import ONDA.domain.challenge.entity.ChallengePost;
import ONDA.domain.challenge.entity.ChallengePostImage;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChallengePostResponse {
    @Schema(description = "작성자", example = "홍길동")
    private String author;

    @Schema(description = "챌린지 제목", example = "영어 7일 챌린지")
    private String title;

    @Schema(description = "이미지", example = "[https://example.com/image1.png,https://example.com/image2.png]")
    private List<String> images = new ArrayList<>();

    @Schema(description = "챌린지 인증날짜", example = "2025-09-08")
    private LocalDate createDate;

    public ChallengePostResponse(ChallengePost post) {
        this.author = post.getAuthor().getNickname();
        this.title = post.getChallenge().getTitle();
        this.createDate = post.getCreateDate();

        List<ChallengePostImage> postImages = post.getImages();
        for(ChallengePostImage challengePostImage : postImages){
            images.add(challengePostImage.getUrl());
        }
    }
}
