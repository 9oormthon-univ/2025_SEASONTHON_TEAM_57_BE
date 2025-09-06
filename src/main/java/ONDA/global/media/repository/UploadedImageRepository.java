package ONDA.global.media.repository;

import ONDA.global.media.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadedImageRepository extends JpaRepository<PostImage, Long> {
}
