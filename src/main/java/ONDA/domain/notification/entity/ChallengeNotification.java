package ONDA.domain.notification.entity;

import ONDA.domain.challenge.entity.Challenge;
import ONDA.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("Challenge")
@RequiredArgsConstructor
public class ChallengeNotification extends Notification {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    public ChallengeNotification(Member member, String message, LocalDateTime createdAt, Challenge challenge) {
        super(member, message, createdAt);
        this.challenge = challenge;
    }
}
