package ONDA.domain.challenge.entity;

import ONDA.domain.member.entity.Member;
import ONDA.global.media.entity.ChallengeImage;
import ONDA.global.media.entity.PostImage;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengeImage> images = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ReviewStatus reviewStatus;

    @Enumerated(EnumType.STRING)
    private ProgressStatus progressStatus;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDateTime createdAt;

    private boolean reputationCalculated = false;

    @Builder.Default
    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengePost> challengePosts = new ArrayList<>();

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengeCategory> categories = new ArrayList<>();

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengeImage> images = new ArrayList<>();

    public void markReputationCalculated() {
        this.reputationCalculated = true;
    }

}
