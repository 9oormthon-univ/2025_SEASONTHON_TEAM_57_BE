package ONDA.domain.talent.post.repository;

import ONDA.domain.talent.post.entity.TalentPost;
import ONDA.domain.talent.post.entity.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TalentPostRepository extends JpaRepository<TalentPost, Long> {

    //전체 조회
    List<TalentPost> findAllByOrderByCreatedAtDesc();

    // 카테고리별
    @Query("SELECT DISTINCT p FROM TalentPost p " +
            "JOIN p.categories pc " +
            "WHERE pc.category.id = :categoryId " +
            "ORDER BY p.createdAt DESC")
    List<TalentPost> findByCategoryId(@Param("categoryId") Long categoryId);

    // 댓글많은순
    @Query("SELECT p FROM TalentPost p " +
            "WHERE p.status = 'OPEN' " +
            "ORDER BY SIZE(p.comments) DESC, p.createdAt DESC")
    List<TalentPost> findHotPosts();


    // 작성자별 게시글 조회
    @Query("SELECT p FROM TalentPost p WHERE p.author.id = :authorId ORDER BY p.createdAt DESC")
    List<TalentPost> findByAuthorId(@Param("authorId") Long authorId);
}