package ONDA.global.media.dto;

import ONDA.global.media.entity.ImageUsageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ImageUploadResponse {

    @Schema(description = "이미지 Id", example = "1")
    private Long imageId;

    @Schema(description = "이미지 주소", example = "https://api.wownd.store/api/media/images/...")
    private String imageUrl;
}
