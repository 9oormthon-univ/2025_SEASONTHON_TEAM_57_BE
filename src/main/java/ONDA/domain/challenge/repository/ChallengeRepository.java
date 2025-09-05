package ONDA.domain.challenge.repository;

import ONDA.domain.challenge.entity.Challenge;
import ONDA.domain.challenge.entity.ProgressStatus;
import ONDA.domain.challenge.entity.ReviewStatus;
import ONDA.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge,Long> {
    Optional<Challenge> findByIdAndProgressStatus(Long ChallengeId, ProgressStatus progressStatus);
    List<Challenge> findByAuthor(Member member);
    List<Challenge> findByReviewStatus(ReviewStatus reviewStatus);
    List<Challenge> findByProgressStatusAndReviewStatus(ProgressStatus progressStatus,ReviewStatus reviewStatus);

}
