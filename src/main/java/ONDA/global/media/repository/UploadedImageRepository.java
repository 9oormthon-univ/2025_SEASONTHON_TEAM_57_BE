package ONDA.global.media.repository;

import ONDA.global.media.entity.ImageUsageType;
import ONDA.global.media.entity.UploadedImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UploadedImageRepository extends JpaRepository<UploadedImage, Long> {
    
    List<UploadedImage> findByUsageTypeAndReferenceId(ImageUsageType usageType, Long referenceId);
    
    @Query("SELECT ui FROM UploadedImage ui WHERE ui.usageType = :usageType AND ui.referenceId = :referenceId ORDER BY ui.uploadedAt DESC")
    List<UploadedImage> findByUsageTypeAndReferenceIdOrderByUploadedAtDesc(
            @Param("usageType") ImageUsageType usageType, 
            @Param("referenceId") Long referenceId
    );
    
    List<UploadedImage> findByUploaderId(Long uploaderId);
    
    void deleteByUsageTypeAndReferenceId(ImageUsageType usageType, Long referenceId);
}
