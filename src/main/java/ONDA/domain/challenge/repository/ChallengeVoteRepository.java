package ONDA.domain.challenge.repository;

import ONDA.domain.challenge.entity.Challenge;
import ONDA.domain.challenge.entity.ChallengeVote;
import ONDA.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeVoteRepository extends JpaRepository<ChallengeVote, Long> {
    boolean existsByChallengeAndVoter(Challenge challenge, Member voter);
    long countByChallengeAndParticipant(Challenge challenge, Member participant);
}
