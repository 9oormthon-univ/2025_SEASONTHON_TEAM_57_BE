package ONDA.domain.challenge.service.inf;

import ONDA.domain.challenge.entity.Challenge;
import ONDA.domain.challenge.entity.ProgressStatus;
import ONDA.domain.challenge.repository.ChallengeRepository;
import ONDA.domain.notification.service.inf.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ChallengeStatusScheduler {

    private final ChallengeRepository challengeRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정 실행
    public void updateChallengeStatuses() {
        List<Challenge> challenges = challengeRepository.findAll();
        LocalDate today = LocalDate.now();
        for (Challenge c : challenges) {
            if (today.isBefore(c.getStartDate())) {
                c.setProgressStatus(ProgressStatus.NOT_STARTED);
            } else if (!today.isAfter(c.getEndDate())) {
                c.setProgressStatus(ProgressStatus.ONGOING);
            } else {
                c.setProgressStatus(ProgressStatus.ENDED);
                notificationService.sendChallengeEndedNotification(c.getAuthor().getId(), c.getId());
            }
        }
        challengeRepository.saveAll(challenges);
    }
}