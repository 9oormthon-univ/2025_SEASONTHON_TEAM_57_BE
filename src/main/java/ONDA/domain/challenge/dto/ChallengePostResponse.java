package ONDA.domain.challenge.dto;

import ONDA.domain.challenge.entity.ChallengePost;
import ONDA.domain.talent.post.dto.TalentPostResponse;
import ONDA.domain.talent.post.entity.TalentPost;
import ONDA.global.media.dto.ImageUploadResponse;
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

    @Schema(description = "챌린지 인증 글 이미지들")
    private List<ImageUploadResponse> images;

//    @Schema(description = "이미지", example = "[\"https://example.com/image1.png\",\"https://example.com/image2.png\"]")
//    private List<String> images = new ArrayList<>();

    @Schema(description = "챌린지 인증날짜", example = "2025-09-08")
    private LocalDate createDate;


    public ChallengePostResponse(ChallengePost post) {
        this.author = post.getAuthor().getNickname();
        this.title = post.getChallenge().getTitle();
        this.createDate = post.getCreateDate();
        this.images = post.getImages().stream()
                .map(im -> ImageUploadResponse.builder()
                        .imageId(im.getId())
                        .imageUrl(im.getImageUrl())
                        .build())
                .toList();

//        List<ChallengePostImage> postImages = post.getImages();
//        for(ChallengePostImage challengePostImage : postImages){
//            images.add(challengePostImage.getUrl());
//        }
    }
}
