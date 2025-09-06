package ONDA.global.media.dto;

import ONDA.global.media.entity.ImageUsageType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ImageUploadResponse {
    private Long imageId;
    private String imageName;
    private String imageUrl;
    private ImageUsageType usageType;
    private LocalDateTime uploadedAt;
}
