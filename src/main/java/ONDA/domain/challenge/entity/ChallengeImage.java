package ONDA.domain.challenge.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChallengeImage {
    @Id
    @GeneratedValue
    @Column(name = "challengeImage_id", unique = true, nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    private String url;

    public static ChallengeImage of(Challenge challenge, String url) {
        ChallengeImage image = new ChallengeImage();
        image.challenge = challenge;
        image.url = url;
        return image;
    }
}