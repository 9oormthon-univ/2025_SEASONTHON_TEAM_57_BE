package ONDA.domain.challenge.repository;

import ONDA.domain.challenge.entity.Challenge;
import ONDA.domain.challenge.entity.ChallengeCategory;
import ONDA.domain.challenge.entity.ProgressStatus;
import ONDA.domain.challenge.entity.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChallengeCategoryRepository extends JpaRepository<ChallengeCategory,Long> {

    @Query("SELECT cc.challenge FROM ChallengeCategory cc " +
            "WHERE cc.category.id = :categoryId AND cc.challenge.reviewStatus = :status")
    List<Challenge> findChallengesByCategoryIdAndReviewStatus(
            @Param("categoryId") Long categoryId,
            @Param("status") ReviewStatus status);

    @Query("SELECT cc.challenge FROM ChallengeCategory cc " +
            "WHERE cc.category.id = :categoryId " +
            "AND cc.challenge.reviewStatus = :reviewStatus " +
            "AND cc.challenge.progressStatus IN :progressStatuses ORDER BY cc.id DESC")
    List<Challenge> findChallengesByCategoryIdAndStatusesOrderByIdDesc(@Param("categoryId") Long categoryId,
                                                          @Param("reviewStatus") ReviewStatus reviewStatus,
                                                          @Param("progressStatuses") List<ProgressStatus> progressStatuses);
}
