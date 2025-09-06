package ONDA.domain.reputation.service;

import ONDA.domain.challenge.entity.Challenge;
import ONDA.domain.challenge.repository.ChallengeRepository;
import ONDA.domain.reputation.service.inf.ReputationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReputationScheduler {

    private final ChallengeRepository challengeRepository;
    private final ReputationService reputationService;

    @Scheduled(cron = "*/1 * * * * ?") // 매일 자정
    @Transactional
    public void processFinishedChallenges() {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.minusDays(7);

        List<Challenge> challenges = challengeRepository.findByEndDate(endDate);

        for (Challenge challenge : challenges) {
            if (challenge.isReputationCalculated()) continue;

            reputationService.assignReputationScores(challenge.getId());

            challenge.markReputationCalculated();
            challengeRepository.save(challenge);
        }
    }
}
