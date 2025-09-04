package ONDA.domain.challenge.repository;

import ONDA.domain.challenge.entity.ChallengePost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengePostRepository extends JpaRepository<ChallengePost,Long> {
}
