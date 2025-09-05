package ONDA.domain.challenge.entity;

import ONDA.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Setter
public class ChallengeVote {
    @Id
    @GeneratedValue
    @Column(name = "vote_id", unique = true, nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voter_id", nullable = false)
    private Member voter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    private Member participant;

    private LocalDateTime votedAt;

    // 제약조건: 한 명은 한 챌린지에 한 번만 투표
    @PrePersist
    public void prePersist() {
        this.votedAt = LocalDateTime.now();
    }
}
