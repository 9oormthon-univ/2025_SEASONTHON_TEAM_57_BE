package ONDA.domain.challenge.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengePostImage {
    @Id
    @GeneratedValue
    @Column(name = "challengePostImage_id", unique = true, nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_post_id")
    private ChallengePost post;

    private String url;

    public static ChallengePostImage of(ChallengePost post, String url) {
        ChallengePostImage image = new ChallengePostImage();
        image.post = post;
        image.url = url;
        return image;
    }
}