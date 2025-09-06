package ONDA.global.media.entity;


import ONDA.domain.challenge.entity.Challenge;
import ONDA.domain.member.entity.Member;
import ONDA.domain.talent.post.entity.TalentPost;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "challenge_image")
public class ChallengeImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl; // UUID.확장자 (abc123-def456.jpg)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id")
    private Member uploader;

    @CreationTimestamp
    private LocalDateTime uploadedAt;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talent_post_id")
    private Challenge challenge;

    @Builder
    private ChallengeImage(String imageUrl, Member uploader, Challenge challenge) {
        this.imageUrl = imageUrl;
        this.uploader = uploader;
        this.challenge = challenge;
    }

}

