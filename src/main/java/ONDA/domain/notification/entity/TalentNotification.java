package ONDA.domain.notification.entity;

import ONDA.domain.challenge.entity.Challenge;
import ONDA.domain.member.entity.Member;
import ONDA.domain.talent.post.entity.TalentPost;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("Talent")
@RequiredArgsConstructor
public class TalentNotification extends Notification{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talentPost_id")
    private TalentPost talentPost;

    public TalentNotification(Member member, String message, LocalDateTime createdAt, TalentPost talentPost) {
        super(member, message, createdAt);
        this.talentPost = talentPost;
    }
}
