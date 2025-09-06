package ONDA.global.media.service;

import ONDA.domain.member.entity.Member;
import ONDA.domain.talent.post.entity.TalentPost;
import ONDA.domain.talent.post.repository.TalentPostRepository;
import ONDA.domain.talent.post.service.TalentPostService;
import ONDA.global.exception.BusinessException;
import ONDA.global.exception.ErrorCode;
import ONDA.global.media.entity.ImageUsageType;
import ONDA.global.media.entity.PostImage;
import ONDA.global.media.repository.UploadedImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MediaService {

    private final UploadedImageRepository uploadedImageRepository;
    private final TalentPostService talentPostService;

    @Value("${app.backend-base-url}")
    private String backUrl;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private final List<String> allowedContentTypes = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"
    );

    private final long maxFileSize = 10 * 1024 * 1024; // 10MB

    @Transactional
    public PostImage uploadImage(MultipartFile file, Member uploader, ImageUsageType usageType, Long postId) {
        validateFile(file);
        
        String fileName = generateFileName(file);
        String filePath = saveFile(file, fileName);

        TalentPost post = talentPostService.getPostById(postId);

        PostImage postImage = PostImage.builder()
                .imageUrl(fileName)
                .uploader(uploader)
                .post(post)
                .build();

        if (usageType == ImageUsageType.TALENT_POST_IMAGE) {
            post.addImages(postImage);
        }

        return uploadedImageRepository.save(postImage);
    }

    @Transactional
    public List<PostImage> uploadImages(List<MultipartFile> files, Member uploader,
                                        ImageUsageType usageType, Long referenceId) {
        if (files.size() > 5) {
            throw new BusinessException(ErrorCode.TOO_MANY_FILES);
        }

        //프로필은 1개만 가능
        if (ImageUsageType.PROFILE_IMAGE == usageType && files.size() != 1) {
            throw new BusinessException(ErrorCode.PROFILE_IMAGE_SINGLE_REQUIRED);
        }
        
        return files.stream()
                .map(file -> uploadImage(file, uploader, usageType, referenceId))
                .toList();
    }

    public String getImageUrl(String imageName) {
        return backUrl + "/api/media/images/" + imageName;
    }

    public byte[] getImageBytes(String imageName) {
        try {
            Path imagePath = Paths.get(uploadDir, imageName);
            if (!Files.exists(imagePath)) {
                throw new BusinessException(ErrorCode.IMAGE_NOT_FOUND);
            }
            return Files.readAllBytes(imagePath);
        } catch (IOException e) {
            log.error("이미지 읽기 실패: {}", imageName, e);
            throw new BusinessException(ErrorCode.IMAGE_READ_FAILED);
        }
    }

    @Transactional
    public void deleteImage(Long imageId, Member member) {
        PostImage image = uploadedImageRepository.findById(imageId)
                .orElseThrow(() -> new BusinessException(ErrorCode.IMAGE_NOT_FOUND));
        
        if (!image.getUploader().getId().equals(member.getId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        
        deleteFileFromStorage(image.getImageUrl());
        uploadedImageRepository.delete(image);
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException(ErrorCode.EMPTY_FILE);
        }
        
        if (file.getSize() > maxFileSize) {
            throw new BusinessException(ErrorCode.FILE_SIZE_EXCEEDED);
        }
        
        String contentType = file.getContentType();
        if (contentType == null || !allowedContentTypes.contains(contentType.toLowerCase())) {
            throw new BusinessException(ErrorCode.INVALID_FILE_TYPE);
        }
    }

    private String generateFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new BusinessException(ErrorCode.INVALID_FILE_NAME);
        }
        
        String extension = getFileExtension(originalFilename);
        return UUID.randomUUID().toString() + "." + extension;
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new BusinessException(ErrorCode.INVALID_FILE_NAME);
        }
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }

    private String saveFile(MultipartFile file, String fileName) {
        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            //Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath.toFile());

            return filePath.toString();
        } catch (IOException e) {
            log.error("파일 저장 실패: {}", fileName, e);
            throw new BusinessException(ErrorCode.FILE_SAVE_FAILED);
        }
    }

    private void deleteFileFromStorage(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir, fileName);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException e) {
            log.warn("파일 삭제 실패: {}", fileName, e);
        }
    }
}
