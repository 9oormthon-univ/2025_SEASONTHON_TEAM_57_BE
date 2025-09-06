package ONDA.domain.talent.post.entity;

import ONDA.domain.member.entity.Member;
import ONDA.domain.talent.comment.entity.Comment;
import ONDA.global.media.entity.PostImage;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TalentPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "talent_post_id")
    private Long id;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;

    @Enumerated(EnumType.STRING)
    private PostType type;

    private String title;

    private String content;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostCategory> categories = new ArrayList<>();

    private int price;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> images = new ArrayList<>();

    @Builder
    public TalentPost(Member author, PostType type, String title, String content, int price) {
        this.author = author;
        this.type = type;
        this.title = title;
        this.content = content;
        this.status = PostStatus.OPEN;
        this.price = price;
    }

    public void update(PostType type, String title, String content, PostStatus status, int price) {
        this.type = type;
        this.title = title;
        this.content = content;
        this.status = status;
        this.price = price;
    }

    public void updateCategories(List<PostCategory> categories) {
        this.categories.clear();
        this.categories.addAll(categories);
    }

    public void addImages(PostImage image) {
        images.add(image);
        image.setPost(this);
    }
}