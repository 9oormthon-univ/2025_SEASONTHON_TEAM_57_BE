package ONDA.global.media.entity;


import ONDA.domain.member.entity.Member;
import ONDA.domain.talent.post.entity.TalentPost;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "uploaded_image")
public class UploadedImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl; // UUID.확장자 (abc123-def456.jpg)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id")
    private Member uploader;

    @Enumerated(EnumType.STRING)
    private ImageUsageType usageType;

    @Column(name = "reference_id")
    private Long referenceId; // 사용되는 엔티티의 ID

    @CreationTimestamp
    private LocalDateTime uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private TalentPost post;


    @Builder
    private UploadedImage(String imageUrl, Member uploader, ImageUsageType usageType, Long referenceId) {
        this.imageUrl = imageUrl;
        this.uploader = uploader;
        this.usageType = usageType;
        this.referenceId = referenceId;
    }
}

