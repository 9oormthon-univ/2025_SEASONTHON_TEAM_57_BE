package ONDA.domain.talent.post.service;

import ONDA.domain.member.entity.Member;
import ONDA.domain.member.repository.MemberRepository;
import ONDA.domain.talent.post.dto.TalentPostCreateRequest;
import ONDA.domain.talent.post.dto.TalentPostListResponse;
import ONDA.domain.talent.post.dto.TalentPostResponse;
import ONDA.domain.talent.post.dto.TalentPostUpdateRequest;
import ONDA.domain.talent.post.entity.PostCategory;
import ONDA.domain.talent.post.entity.TalentPost;
import ONDA.domain.talent.post.repository.TalentPostRepository;
import ONDA.global.category.Category;
import ONDA.global.category.CategoryRepository;
import ONDA.global.exception.BusinessException;
import ONDA.global.exception.ErrorCode;
import ONDA.global.exception.NotFoundMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TalentPostService {

    private final TalentPostRepository talentPostRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public TalentPostResponse create(Long memberId, TalentPostCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        TalentPost post = TalentPost.builder()
                .author(member)
                .type(request.getType())
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            List<Category> categories = categoryRepository.findByIdIn(request.getCategoryIds());
            List<PostCategory> postCategories = categories.stream()
                    .map(category -> PostCategory.builder()
                            .post(post)
                            .category(category)
                            .type(request.getType())
                            .build())
                    .toList();
            post.updateCategories(postCategories);
        }

        TalentPost savedPost = talentPostRepository.save(post);
        return TalentPostResponse.from(savedPost);
    }

    public TalentPostResponse getById(Long postId) {
        TalentPost post = talentPostRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        return TalentPostResponse.from(post);
    }

    public List<TalentPostListResponse> getByCategory(Long categoryId) {
        List<TalentPost> posts = talentPostRepository.findByCategoryId(categoryId);
        return posts.stream()
                .map(TalentPostListResponse::from)
                .toList();
    }

    public List<TalentPostListResponse> getHotPost() {
        List<TalentPost> hotPosts = talentPostRepository.findHotPosts();

        return hotPosts.stream()
                .limit(10)
                .map(TalentPostListResponse::from)
                .toList();
    }

    public List<TalentPostListResponse> getRecommended(Long memberId) {
        // 일단은 최신순으로
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<TalentPost> recommendedPosts = talentPostRepository.findAll(pageRequest).getContent();

        return recommendedPosts.stream()
                .map(TalentPostListResponse::from)
                .toList();
    }

    @Transactional
    public TalentPostResponse update(Long postId, Long memberId, TalentPostUpdateRequest request) {
        TalentPost post = talentPostRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (!post.getAuthor().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.OWNER_MISMATCH);
        }

        post.update(
                request.getType() != null ? request.getType() : post.getType(),
                request.getTitle() != null ? request.getTitle() : post.getTitle(),
                request.getContent() != null ? request.getContent() : post.getContent(),
                request.getStatus() != null ? request.getStatus() : post.getStatus()
        );

        if (request.getCategoryIds() != null) {
            List<Category> categories = categoryRepository.findByIdIn(request.getCategoryIds());
            List<PostCategory> postCategories = categories.stream()
                    .map(category -> PostCategory.builder()
                            .post(post)
                            .category(category)
                            .type(post.getType())
                            .build())
                    .toList();
            post.updateCategories(postCategories);
        }

        return TalentPostResponse.from(post);
    }

    @Transactional
    public void delete(Long postId, Long memberId) {
        TalentPost post = talentPostRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));

        if (!post.getAuthor().getId().equals(memberId)) {
            throw new BusinessException(ErrorCode.OWNER_MISMATCH);
        }

        talentPostRepository.delete(post);
    }

    public List<TalentPostListResponse> getMyPosts(Long memberId) {
        List<TalentPost> myPosts = talentPostRepository.findByAuthorId(memberId);
        return myPosts.stream()
                .map(TalentPostListResponse::from)
                .toList();
    }
}
