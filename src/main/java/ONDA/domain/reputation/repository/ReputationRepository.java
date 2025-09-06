package ONDA.domain.reputation.repository;

import ONDA.domain.member.entity.Member;
import ONDA.domain.reputation.entity.Reputation;
import ONDA.global.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReputationRepository extends JpaRepository<Reputation, Long> {
    @Query("SELECT COALESCE(SUM(r.score), 0) FROM Reputation r WHERE r.member.id = :memberId")
    int findTotalScoreByMember(@Param("memberId") Long memberId);

    Optional<Reputation> findByMemberAndCategory(Member member, Category category);
    List<Reputation> findReputationByMemberAndCategory(Member memberId, Category category);
}
