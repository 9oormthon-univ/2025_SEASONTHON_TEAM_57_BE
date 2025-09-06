package ONDA.domain.notification.service.inf;

import ONDA.domain.notification.dto.NotificationResponse;
import ONDA.global.response.ApiResponse;

import java.util.List;

public interface NotificationService {
    void sendChallengeEndedNotification(Long memberId, Long challengeId);
    ApiResponse<List<NotificationResponse>> getMyNotifications(Long memberId);
}
