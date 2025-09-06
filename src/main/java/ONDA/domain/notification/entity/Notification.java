package ONDA.domain.notification.entity;

import ONDA.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@RequiredArgsConstructor
@DiscriminatorColumn(name = "Category")
public abstract class Notification {
    @Id
    @GeneratedValue
    @Column(name = "notification_id", unique = true, nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String message;

    private LocalDateTime createdAt;

    protected Notification(Member member, String message, LocalDateTime createdAt) {
        this.member = member;
        this.message = message;
        this.createdAt = createdAt;
    }
}
