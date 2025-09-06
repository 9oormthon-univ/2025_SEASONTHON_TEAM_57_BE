package ONDA.domain.notification.dto;

import ONDA.domain.notification.entity.Notification;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationResponse {
    @Schema(description = "알림 메시지", example = "'ㅇㅇ' 챌린지가 종료되었습니다.")
    private final String message;

    @Schema(description = "알림 생성 시각", example = "2025-09-01 00:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;

    public NotificationResponse(Notification notification) {
        this.message = notification.getMessage();
        this.createdAt = notification.getCreatedAt();
    }
}