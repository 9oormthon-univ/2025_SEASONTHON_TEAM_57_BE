package ONDA.domain.notification.controller;

import ONDA.domain.notification.dto.NotificationResponse;
import ONDA.domain.notification.service.inf.NotificationService;
import ONDA.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification", description = "알림 API")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/my")
    @Operation(summary = "내 알림 목록 조회", description = "내 알림 목록을 조회합니다")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getMyNotifications(
            @AuthenticationPrincipal Long memberId) {

        ApiResponse<List<NotificationResponse>> notifications = notificationService.getMyNotifications(memberId);

        return ResponseEntity.status(200).body(notifications);
    }
}
