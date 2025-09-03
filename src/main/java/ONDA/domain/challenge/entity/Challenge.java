package ONDA.domain.challenge.entity;

import ONDA.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter @Setter
public class Challenge {
    @Id
    @GeneratedValue
    @Column(name = "challenge_id", unique = true, nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;

    private String title;

    private String content;

    private String image;

    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;

    @Enumerated(EnumType.STRING)
    private ProgressStatus progressStatus;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDateTime createdAt;
}
