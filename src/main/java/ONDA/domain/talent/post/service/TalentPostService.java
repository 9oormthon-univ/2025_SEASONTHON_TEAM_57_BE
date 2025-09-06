package ONDA.domain.talent.post.service;

import ONDA.domain.member.entity.Member;
import ONDA.domain.member.repository.MemberRepository;
import ONDA.domain.talent.post.dto.TalentPostCreateRequest;
import ONDA.domain.talent.post.dto.TalentPostResponse;
import ONDA.domain.talent.post.dto.TalentPostUpdateRequest;
import ONDA.domain.talent.post.entity.PostCategory;
import ONDA.domain.talent.post.entity.PostType;
import ONDA.domain.talent.post.entity.TalentPost;
import ONDA.domain.talent.post.repository.TalentPostRepository;
import ONDA.global.category.Category;
import ONDA.global.category.CategoryRepository;
import ONDA.global.exception.BusinessException;
import ONDA.global.exception.ErrorCode;
import ONDA.global.exception.NotFoundMemberException;
import ONDA.global.media.entity.PostImage;
import ONDA.global.media.repository.UploadedImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TalentPostService {

    private final TalentPostRepository talentPostRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final UploadedImageRepository uploadedImageRepository;

    @Transactional
    public TalentPostResponse create(Long memberId, TalentPostCreateRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        TalentPost post = TalentPost.builder()
                .author(member)
                .type(request.getType())
                .title(request.getTitle())
                .content(request.getContent())
                .price(request.getPrice())
                .build();

        List<PostCategory> postCategories = createPostCategories(post, request.getLearnCategoryIds(), request.getTeachCategoryIds());
        if (!postCategories.isEmpty()) {
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

    public List<TalentPostResponse> getByCategory(Long categoryId) {
        List<TalentPost> posts = talentPostRepository.findByCategoryId(categoryId);
        return posts.stream()
                .map(post -> TalentPostResponse.from(post))
                .toList();
    }

    public List<TalentPostResponse> getHotPost() {
        List<TalentPost> hotPosts = talentPostRepository.findHotPosts();

        return hotPosts.stream()
                .limit(10)
                .map(post -> TalentPostResponse.from(post))
                .toList();
    }

    public List<TalentPostResponse> getRecommended(Long memberId) {
        // 일단 최신순으로
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        PageRequest pageRequest = PageRequest.of(0, 10);
        List<TalentPost> recommendedPosts = talentPostRepository.findAll(pageRequest).getContent();

        return recommendedPosts.stream()
                .map(post -> TalentPostResponse.from(post))
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
                request.getStatus() != null ? request.getStatus() : post.getStatus(),
                request.getPrice() != null ? request.getPrice() : post.getPrice()
        );

        if (request.getLearnCategoryIds() != null || request.getTeachCategoryIds() != null) {
            List<PostCategory> postCategories = createPostCategories(post, request.getLearnCategoryIds(), request.getTeachCategoryIds());
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

    public List<TalentPostResponse> getMyPosts(Long memberId) {
        List<TalentPost> myPosts = talentPostRepository.findByAuthorId(memberId);
        return myPosts.stream()
                .map(post -> TalentPostResponse.from(post))
                .toList();
    }

    private List<PostCategory> createPostCategories(TalentPost post, List<Long> learnCategoryIds, List<Long> teachCategoryIds) {
        List<PostCategory> postCategories = new ArrayList<>();

        if (learnCategoryIds != null && !learnCategoryIds.isEmpty()) {
            List<Category> learnCategories = categoryRepository.findByIdIn(learnCategoryIds);
            List<PostCategory> learnPostCategories = learnCategories.stream()
                    .map(category -> PostCategory.builder()
                            .post(post)
                            .category(category)
                            .type(PostType.LEARN)
                            .build())
                    .toList();
            postCategories.addAll(learnPostCategories);
        }

        if (teachCategoryIds != null && !teachCategoryIds.isEmpty()) {
            List<Category> teachCategories = categoryRepository.findByIdIn(teachCategoryIds);
            List<PostCategory> teachPostCategories = teachCategories.stream()
                    .map(category -> PostCategory.builder()
                            .post(post)
                            .category(category)
                            .type(PostType.TEACH)
                            .build())
                    .toList();
            postCategories.addAll(teachPostCategories);
        }

        return postCategories;
    }

    @Transactional
    public void uploadImages(PostImage postImage, TalentPost talentPost) {
        talentPost.getImages().add(postImage);
        postImage.setPost(talentPost);
    }

    public TalentPost getPostById(Long postId) {
        return talentPostRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.POST_NOT_FOUND));
    }
    public List<TalentPostResponse> getAll() {
        List<TalentPost> posts = talentPostRepository.findAllByOrderByCreatedAtDesc();
        return posts.stream()
                .limit(20)
                .map(TalentPostResponse::from)
                .toList();
    }

//    public List<UploadedImage> getImages(Long postId) {
//        return uploadedImageRepository.findByUsageTypeAndReferenceId(ImageUsageType.TALENT_POST_IMAGE, postId);
//    }
}
