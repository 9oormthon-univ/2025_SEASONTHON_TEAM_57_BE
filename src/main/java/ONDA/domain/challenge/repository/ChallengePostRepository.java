package ONDA.domain.challenge.repository;

import ONDA.domain.challenge.entity.Challenge;
import ONDA.domain.challenge.entity.ChallengePost;
import ONDA.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ChallengePostRepository extends JpaRepository<ChallengePost,Long> {
    List<ChallengePost> findByAuthor(Member member);
    List<ChallengePost> findByAuthorAndChallenge(Member member, Challenge challenge);
    List<ChallengePost> findByChallengeIdAndAuthorId(Long challengeId, Long memberId);
    List<ChallengePost> findByAuthorAndCreateDate(Member member, LocalDate targetDate);
    @Query("SELECT DISTINCT p.author FROM ChallengePost p WHERE p.challenge.id = :challengeId")
    List<Member> findDistinctAuthorsByChallengeId(@Param("challengeId") Long challengeId);

    List<ChallengePost> findByChallenge(Challenge challenge);
}
