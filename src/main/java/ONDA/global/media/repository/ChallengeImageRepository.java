package ONDA.global.media.repository;

import ONDA.domain.challenge.entity.Challenge;
import ONDA.global.media.entity.ChallengeImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeImageRepository extends JpaRepository<ChallengeImage, Long> {
}
