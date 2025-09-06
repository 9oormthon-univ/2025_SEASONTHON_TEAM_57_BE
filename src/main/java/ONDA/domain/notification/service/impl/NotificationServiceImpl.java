package ONDA.domain.notification.service.impl;

import ONDA.domain.challenge.entity.Challenge;
import ONDA.domain.challenge.entity.ChallengePost;
import ONDA.domain.challenge.entity.ProgressStatus;
import ONDA.domain.challenge.repository.ChallengePostRepository;
import ONDA.domain.challenge.repository.ChallengeRepository;
import ONDA.domain.member.entity.Member;
import ONDA.domain.member.repository.MemberRepository;
import ONDA.domain.notification.dto.NotificationResponse;
import ONDA.domain.notification.entity.ChallengeNotification;
import ONDA.domain.notification.entity.Notification;
import ONDA.domain.notification.repository.NotificationRepository;
import ONDA.domain.notification.service.inf.NotificationService;
import ONDA.global.exception.BusinessException;
import ONDA.global.exception.ErrorCode;
import ONDA.global.exception.NotFoundMemberException;
import ONDA.global.response.ApiResponse;
import ONDA.global.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;

    @Override
    public void sendChallengeEndedNotification(Long memberId, Long challengeId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        List<Challenge> challenge = challengeRepository.findByAuthorAndProgressStatus(member, ProgressStatus.ENDED);

        for(Challenge c : challenge){
            if (notificationRepository.findByMemberAndChallenge(member, c)) continue;
            String message = String.format("'%s' 챌린지가 종료되었습니다.", c.getTitle());

            ChallengeNotification notification = new ChallengeNotification(member, message, LocalDateTime.now(),c);

            notificationRepository.save(notification);
        }
    }

    @Override
    public ApiResponse<List<NotificationResponse>> getMyNotifications(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        List<NotificationResponse> responses = notificationRepository
                .findByMemberOrderByCreatedAtDesc(member).stream()
                .map(NotificationResponse::new)
                .toList();

        return ApiResponse.success(ResponseCode.SUCCESS, responses);
    }
}
