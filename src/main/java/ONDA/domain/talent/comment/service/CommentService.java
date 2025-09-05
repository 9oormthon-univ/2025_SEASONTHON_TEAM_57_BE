package ONDA.domain.talent.comment.service;

import ONDA.domain.member.entity.Member;
import ONDA.domain.member.repository.MemberRepository;
import ONDA.domain.talent.comment.dto.CommentCreateRequest;
import ONDA.domain.talent.comment.dto.CommentResponse;
import ONDA.domain.talent.comment.dto.CommentUpdateRequest;
import ONDA.domain.talent.comment.entity.Comment;
import ONDA.domain.talent.comment.repository.CommentRepository;
import ONDA.domain.talent.post.entity.TalentPost;
import ONDA.domain.talent.post.repository.TalentPostRepository;
import ONDA.global.exception.BusinessException;
import ONDA.global.exception.ErrorCode;
import ONDA.global.exception.NotFoundMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final TalentPostRepository talentPostRepository;

    @Transactional
    public CommentResponse create(Long postId, Long memberId, CommentCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        TalentPost post = talentPostRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        Comment parent = null;
        if (request.getParentId() != null) {
            parent = commentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.PARENT_NOT_FOUND));

            // 부모 댓글이 같은 게시글에 속하는지 확인
            if (!parent.getPost().getId().equals(postId)) {
                throw new BusinessException(ErrorCode.COMMENT_POST_MISMATCH);
            }
        }

        Comment comment = Comment.builder()
                .author(member)
                .post(post)
                .parent(parent)
                .content(request.getContent())
                .build();

        Comment savedComment = commentRepository.save(comment);
        return CommentResponse.fromWithoutChildren(savedComment);
    }

    public List<CommentResponse> getCommentsByPost(Long postId) {
        talentPostRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        List<Comment> parentComments = commentRepository.findByPostIdAndParentIsNull(postId);

        return parentComments.stream()
                .map(CommentResponse::from)
                .toList();
    }


    @Transactional
    public CommentResponse update(Long commentId, Long memberId, CommentUpdateRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getAuthor().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.OWNER_MISMATCH);
        }

        comment.updateContent(request.getContent());

        return CommentResponse.fromWithoutChildren(comment);
    }

    @Transactional
    public void delete(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getAuthor().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.OWNER_MISMATCH);
        }

        commentRepository.delete(comment);
    }



    public List<CommentResponse> getMyComments(Long memberId) {
        List<Comment> myComments = commentRepository.findByAuthorId(memberId);

        return myComments.stream()
                .map(CommentResponse::fromWithoutChildren)
                .toList();
    }
}
