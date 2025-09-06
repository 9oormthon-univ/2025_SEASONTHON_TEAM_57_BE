package ONDA.domain.reputation.entity;

import ONDA.domain.member.entity.Member;
import ONDA.global.category.Category;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Setter
public class Reputation {
    @Id
    @GeneratedValue
    @Column(name = "reputation_id", unique = true, nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private int score;

    public void addScore(int points) {
        this.score += points;
    }
}
