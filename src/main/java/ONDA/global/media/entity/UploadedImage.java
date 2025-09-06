package ONDA.global.media.entity;


import ONDA.domain.member.entity.Member;
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

    private String imageName; // UUID.확장자 (abc123-def456.jpg)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id")
    private Member uploader;

    @Enumerated(EnumType.STRING)
    private ImageUsageType usageType;

    @Column(name = "reference_id")
    private Long referenceId; // 사용되는 엔티티의 ID

    @CreationTimestamp
    private LocalDateTime uploadedAt;

    @Builder
    private UploadedImage(String imageName, Member uploader, ImageUsageType usageType, Long referenceId) {
        this.imageName = imageName;
        this.uploader = uploader;
        this.usageType = usageType;
        this.referenceId = referenceId;
    }
}

