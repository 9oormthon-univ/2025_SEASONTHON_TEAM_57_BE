package ONDA.global.media.controller;

import ONDA.auth.infra.jwt.TokenClaims;
import ONDA.domain.member.entity.Member;
import ONDA.domain.member.service.MemberService;
import ONDA.global.exception.BusinessException;
import ONDA.global.exception.ErrorCode;
import ONDA.global.media.dto.ImageUploadResponse;
import ONDA.global.media.entity.ImageUsageType;
import ONDA.global.media.entity.UploadedImage;
import ONDA.global.media.service.MediaService;
import ONDA.global.response.ApiResponse;
import ONDA.global.response.ResponseCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
@Tag(name = "Media", description = "미디어 업로드 API")
public class MediaController {

    private final MediaService mediaService;
    private final MemberService memberService;

    @PostMapping("/upload/single")
    @Operation(summary = "단일 이미지 업로드", description = "하나의 이미지 파일을 업로드합니다.")
    public ApiResponse<ImageUploadResponse> uploadSingleImage(
            @Parameter(description = "업로드할 이미지 파일") @RequestParam("file") MultipartFile file,
            @Parameter(description = "이미지 사용 타입") @RequestParam("usageType") ImageUsageType usageType,
            @Parameter(description = "참조 엔티티 ID (선택사항)") @RequestParam(value = "referenceId", required = false) Long referenceId,
            @AuthenticationPrincipal Long memberId) {

        Member member = memberService.findById(memberId);
        UploadedImage uploadedImage = mediaService.uploadImage(file, member, usageType, referenceId);
        
        ImageUploadResponse response = ImageUploadResponse.builder()
                .imageId(uploadedImage.getId())
                .imageName(uploadedImage.getImageName())
                .imageUrl(mediaService.getImageUrl(uploadedImage.getImageName()))
                .usageType(uploadedImage.getUsageType())
                .uploadedAt(uploadedImage.getUploadedAt())
                .build();

        return ApiResponse.success(ResponseCode.SUCCESS, response);
    }

    @PostMapping("/upload/multiple")
    @Operation(summary = "다중 이미지 업로드", description = "여러 이미지 파일을 한번에 업로드합니다.")
    public ApiResponse<List<ImageUploadResponse>> uploadMultipleImages(
            @Parameter(description = "업로드할 이미지 파일들") @RequestParam("files") MultipartFile[] files,
            @Parameter(description = "이미지 사용 타입") @RequestParam("usageType") ImageUsageType usageType,
            @Parameter(description = "참조 엔티티 ID (선택사항)") @RequestParam(value = "referenceId", required = false) Long referenceId,
            @AuthenticationPrincipal Long memberId) {

        Member member = memberService.findById(memberId);
        List<MultipartFile> fileList = Arrays.asList(files);
        
        List<UploadedImage> uploadedImages = mediaService.uploadImages(fileList, member, usageType, referenceId);
        List<ImageUploadResponse> responses = uploadedImages.stream()
                .map(uploadedImage -> ImageUploadResponse.builder()
                        .imageId(uploadedImage.getId())
                        .imageName(uploadedImage.getImageName())
                        .imageUrl(mediaService.getImageUrl(uploadedImage.getImageName()))
                        .usageType(uploadedImage.getUsageType())
                        .uploadedAt(uploadedImage.getUploadedAt())
                        .build())
                .toList();
        
        return ApiResponse.success(ResponseCode.SUCCESS, responses);
    }

    @GetMapping("/images/{imageName}")
    @Operation(summary = "이미지 조회", description = "업로드된 이미지를 조회합니다.")
    public ResponseEntity<byte[]> getImage(
            @Parameter(description = "이미지 파일명") @PathVariable String imageName) {
        
        try {
            byte[] imageBytes = mediaService.getImageBytes(imageName);
            
            String contentType = determineContentType(imageName);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentLength(imageBytes.length);
            
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (BusinessException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{imageId}")
    @Operation(summary = "이미지 삭제", description = "업로드된 이미지를 삭제합니다.")
    public ApiResponse<Void> deleteImage(
            @Parameter(description = "삭제할 이미지 ID") @PathVariable Long imageId,
            @AuthenticationPrincipal Long memberId) {

        Member member = memberService.findById(memberId);
        mediaService.deleteImage(imageId, member);

        return ApiResponse.success(ResponseCode.SUCCESS, null);
    }

    private String determineContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        
        return switch (extension) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "webp" -> "image/webp";
            default -> "application/octet-stream";
        };
    }
}
