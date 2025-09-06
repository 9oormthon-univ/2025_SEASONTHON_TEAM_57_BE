package ONDA.domain.challenge.entity;

import ONDA.domain.member.entity.Member;
import ONDA.global.media.entity.ChallengePostImage;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@Setter
public class ChallengePost {
    @Id
    @GeneratedValue
    @Column(name = "challengePost_id", unique = true, nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @OneToMany(mappedBy = "challengePost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChallengePostImage> images = new ArrayList<>();

    private LocalDate createDate;

}
