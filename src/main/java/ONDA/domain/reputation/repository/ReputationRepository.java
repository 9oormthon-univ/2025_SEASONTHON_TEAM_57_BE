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
    @Query("SELECT SUM(r.score) FROM Reputation r WHERE r.member.id = :memberId")
    Integer findTotalScoreByMember(@Param("memberId") Long memberId);

    @Query("SELECT r FROM Reputation r WHERE r.member = :member AND r.category = :category")
    Optional<Reputation> findFirstByMemberAndCategory(@Param("member") Member member, @Param("category") Category category);
}
