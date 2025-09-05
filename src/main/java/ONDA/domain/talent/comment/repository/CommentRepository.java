package ONDA.domain.talent.comment.repository;

import ONDA.domain.talent.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    // 특정 게시글의 최상위 댓글들만 조회 (대댓글 제외)
    @Query("SELECT c FROM Comment c " +
           "WHERE c.post.id = :postId AND c.parent IS NULL " +
           "ORDER BY c.createdAt ASC")
    List<Comment> findByPostIdAndParentIsNull(@Param("postId") Long postId);
    
    // 특정 게시글의 최상위 댓글들 페이징 조회
    @Query("SELECT c FROM Comment c " +
           "WHERE c.post.id = :postId AND c.parent IS NULL " +
           "ORDER BY c.createdAt ASC")
    Page<Comment> findByPostIdAndParentIsNull(@Param("postId") Long postId, Pageable pageable);
    
    // 특정 부모 댓글의 대댓글들 조회
    @Query("SELECT c FROM Comment c " +
           "WHERE c.parent.id = :parentId " +
           "ORDER BY c.createdAt ASC")
    List<Comment> findByParentId(@Param("parentId") Long parentId);
    
    // 특정 게시글의 모든 댓글 수 조회
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
    Long countByPostId(@Param("postId") Long postId);
    
    // 특정 사용자의 댓글들 조회
    @Query("SELECT c FROM Comment c WHERE c.author.id = :authorId ORDER BY c.createdAt DESC")
    List<Comment> findByAuthorId(@Param("authorId") Long authorId);
}
