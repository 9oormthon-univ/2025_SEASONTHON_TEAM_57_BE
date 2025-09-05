package ONDA.domain.talent.comment.entity;

import ONDA.domain.member.entity.Member;
import ONDA.domain.talent.post.entity.TalentPost;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private TalentPost post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent; //대댓글용

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public Comment(Member author, TalentPost post, Comment parent, String content) {
        this.author = author;
        this.post = post;
        this.parent = parent;
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}