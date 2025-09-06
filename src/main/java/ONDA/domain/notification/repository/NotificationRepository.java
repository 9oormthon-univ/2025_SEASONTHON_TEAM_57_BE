package ONDA.domain.notification.repository;

import ONDA.domain.challenge.entity.Challenge;
import ONDA.domain.member.entity.Member;
import ONDA.domain.notification.entity.ChallengeNotification;
import ONDA.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByMemberOrderByCreatedAtDesc(Member member);
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END" +
            " FROM ChallengeNotification c WHERE c.member = :member AND c.challenge = :challenge")
    boolean findByMemberAndChallenge(@Param("member") Member member, @Param("challenge") Challenge challenge);
}
