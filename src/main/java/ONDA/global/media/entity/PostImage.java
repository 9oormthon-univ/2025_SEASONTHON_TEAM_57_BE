package ONDA.global.media.entity;


import ONDA.domain.member.entity.Member;
import ONDA.domain.talent.post.entity.TalentPost;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "post_image")
public class PostImage {

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
    private TalentPost post;

    @Builder
    private PostImage(String imageUrl, Member uploader, TalentPost post) {
        this.imageUrl = imageUrl;
        this.uploader = uploader;
        this.post = post;
    }

}

