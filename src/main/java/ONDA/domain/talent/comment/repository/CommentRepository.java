package ONDA.domain.talent.comment.repository;

import ONDA.domain.talent.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    // 최상위 댓글만
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId AND c.parent IS NULL " +
           "ORDER BY c.createdAt ASC")
    List<Comment> findByPostIdAndParentIsNull(@Param("postId") Long postId);
    
    // 작성자별 댓글 조회
    @Query("SELECT c FROM Comment c WHERE c.author.id = :authorId ORDER BY c.createdAt DESC")
    List<Comment> findByAuthorId(@Param("authorId") Long authorId);
}
