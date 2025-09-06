package ONDA.domain.challenge.repository;

import ONDA.domain.challenge.entity.Challenge;
import ONDA.domain.challenge.entity.ProgressStatus;
import ONDA.domain.challenge.entity.ReviewStatus;
import ONDA.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge,Long> {
    Optional<Challenge> findByIdAndProgressStatus(Long ChallengeId, ProgressStatus progressStatus);
    List<Challenge> findByAuthorOrderByIdDesc(Member member);
    List<Challenge> findByReviewStatusOrderByIdDesc(ReviewStatus reviewStatus);
    List<Challenge> findByProgressStatusAndReviewStatus(ProgressStatus progressStatus,ReviewStatus reviewStatus);
    @Query("SELECT c " +
            "FROM Challenge c " +
            "LEFT JOIN c.challengePosts p " +
            "WHERE c.reviewStatus = :reviewStatus " +
            "AND c.progressStatus = :progressStatus " +
            "GROUP BY c " +
            "ORDER BY COUNT(DISTINCT p.author.id) DESC")
    List<Challenge> findOngoingChallengesOrderByParticipants(
            @Param("reviewStatus") ReviewStatus reviewStatus,
            @Param("progressStatus") ProgressStatus progressStatus
    );

    @Query("SELECT c FROM Challenge c " +
            "WHERE c.reviewStatus = :reviewStatus " +
            "AND c.progressStatus IN :progressStatuses ORDER BY c.id DESC")
    List<Challenge> findChallengesByStatusesOrderByIdDesc(@Param("reviewStatus") ReviewStatus reviewStatus,
                                            @Param("progressStatuses") List<ProgressStatus> progressStatuses);

    @Query("SELECT DISTINCT p.challenge " +
            "FROM ChallengePost p " +
            "WHERE p.author = :member ORDER BY p.challenge.id DESC")
    List<Challenge> findChallengesByParticipantOrderByChallengeIdDesc(@Param("member") Member member);

    @Query("SELECT c FROM Challenge c WHERE c.endDate = :endDate")
    List<Challenge> findByEndDate(@Param("endDate") LocalDate endDate);
}
