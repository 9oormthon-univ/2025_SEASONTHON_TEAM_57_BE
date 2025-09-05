package ONDA.domain.challenge.service.impl;

import ONDA.domain.challenge.dto.ChallengeRequest;
import ONDA.domain.challenge.dto.ChallengeResponse;
import ONDA.domain.challenge.entity.Challenge;
import ONDA.domain.challenge.entity.ChallengeCategory;
import ONDA.domain.challenge.entity.ProgressStatus;
import ONDA.domain.challenge.entity.ReviewStatus;
import ONDA.domain.challenge.repository.ChallengeCategoryRepository;
import ONDA.domain.challenge.repository.ChallengeRepository;
import ONDA.domain.challenge.service.inf.ChallengeService;
import ONDA.domain.member.entity.Member;
import ONDA.domain.member.entity.Role;
import ONDA.domain.member.repository.MemberRepository;
import ONDA.global.category.Category;
import ONDA.global.category.CategoryRepository;
import ONDA.global.exception.BusinessException;
import ONDA.global.exception.ErrorCode;
import ONDA.global.exception.NotFoundMemberException;
import ONDA.global.response.ApiResponse;
import ONDA.global.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final ChallengeCategoryRepository challengeCategoryRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public void saveChallenge(Long memberId, ChallengeRequest dto){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        List<Category> categories = categoryRepository.findAllById(dto.getCategoryIds());

        Challenge challenge = dto.toEntity();

        ReviewStatus initialStatus = (member.getRole() == Role.ROLE_ADMIN)
                ? ReviewStatus.APPROVED
                : ReviewStatus.PENDING;
        challenge.setReviewStatus(initialStatus);

        challenge.setProgressStatus(calculateProgressStatus(challenge.getStartDate(), challenge.getEndDate()));
        challenge.setAuthor(member);

        List<ChallengeCategory> challengeCategories = categories.stream()
                .map(cat -> {
                    ChallengeCategory cc = new ChallengeCategory();
                    cc.setChallenge(challenge);
                    cc.setCategory(cat);
                    return cc;
                })
                .collect(Collectors.toList());

        challenge.setCategories(challengeCategories);
        challengeRepository.save(challenge);
    }

    private ProgressStatus calculateProgressStatus(LocalDate startDate, LocalDate endDate) {
        LocalDate today = LocalDate.now();
        if (today.isBefore(startDate)) {
            return ProgressStatus.NOT_STARTED;
        } else if (!today.isAfter(endDate)) {
            return ProgressStatus.ONGOING;
        } else {
            return ProgressStatus.ENDED;
        }
    }

    @Override
    public ApiResponse<List<ChallengeResponse>> getAllChallenges() {
        List<ProgressStatus> progressStatuses = List.of(
                ProgressStatus.NOT_STARTED,
                ProgressStatus.ONGOING
        );

        List<ChallengeResponse> challenges = challengeRepository
                .findChallengesByStatusesOrderByIdDesc(ReviewStatus.APPROVED, progressStatuses).stream()
                .map(ChallengeResponse::new)
                .toList();

        return ApiResponse.success(ResponseCode.SUCCESS, challenges);
    }

    @Override
    public ApiResponse<List<ChallengeResponse>> getMyChallenges(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        List<ChallengeResponse> challenges= challengeRepository.findByAuthorOrderByIdDesc(member).stream()
                .map(ChallengeResponse::new)
                .toList();

        return ApiResponse.success(ResponseCode.SUCCESS, challenges);
    }

    @Override
    public ApiResponse<ChallengeResponse> getChallenge(Long challengeId){
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_CHALLENGE_FOUND));

        ChallengeResponse challengeResponse = new ChallengeResponse(challenge);

        return ApiResponse.success(ResponseCode.SUCCESS, challengeResponse);
    }

    @Override
    public ApiResponse<List<ChallengeResponse>> getChallengesByReviewStatus(ReviewStatus reviewStatus){
        List<ChallengeResponse> challenges = challengeRepository.findByReviewStatusOrderByIdDesc(reviewStatus).stream()
                .map(ChallengeResponse::new)
                .toList();

        return ApiResponse.success(ResponseCode.SUCCESS, challenges);
    }

    @Override
    public ApiResponse<List<ChallengeResponse>> getChallengesByProgressStatus(ProgressStatus progressStatus){
        List<ChallengeResponse> challenges = challengeRepository.findByProgressStatusAndReviewStatus(progressStatus, ReviewStatus.APPROVED).stream()
                .map(ChallengeResponse::new)
                .toList();

        return ApiResponse.success(ResponseCode.SUCCESS, challenges);
    }

    @Override
    public ApiResponse<List<ChallengeResponse>> getOngoingChallengesOrderByParticipants() {
        List<Challenge> challenges = challengeRepository.findOngoingChallengesOrderByParticipants(
                ReviewStatus.APPROVED,
                ProgressStatus.ONGOING
        );

        List<ChallengeResponse> responses = challenges.stream()
                .map(ChallengeResponse::new)
                .toList();

        return ApiResponse.success(ResponseCode.SUCCESS, responses);
    }

    @Override
    public ApiResponse<List<ChallengeResponse>> getChallengesByCategory(Long categoryId){
        List<ProgressStatus> progressStatuses = List.of(
                ProgressStatus.NOT_STARTED,
                ProgressStatus.ONGOING
        );

        List<ChallengeResponse> challenges = challengeCategoryRepository
                .findChallengesByCategoryIdAndStatusesOrderByIdDesc(categoryId, ReviewStatus.APPROVED, progressStatuses).stream()
                .map(ChallengeResponse::new)
                .toList();

        return ApiResponse.success(ResponseCode.SUCCESS, challenges);
    }

    @Override
    public ApiResponse<List<ChallengeResponse>> getMyChallengePosts(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        List<Challenge> challenges = challengeRepository.findChallengesByParticipantOrderByChallengeIdDesc(member);

        List<ChallengeResponse> responses = challenges.stream()
                .map(ChallengeResponse::new)
                .toList();

        return ApiResponse.success(ResponseCode.SUCCESS, responses);
    }

    @Override
    public void pendingChallenge(Long challengeId){
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_CHALLENGE_FOUND));

        challenge.setReviewStatus(ReviewStatus.PENDING);
    }

    @Override
    public void approveChallenge(Long challengeId){
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_CHALLENGE_FOUND));
//        if(!challenge.getReviewStatus().equals(ReviewStatus.PENDING)){
//            throw new BusinessException(ErrorCode.CHALLENGE_ALREADY_REVIEWED);
//        }
        challenge.setReviewStatus(ReviewStatus.APPROVED);
    }

    @Override
    public void rejectChallenge(Long challengeId){
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_CHALLENGE_FOUND));
//        if(!challenge.getReviewStatus().equals(ReviewStatus.PENDING)){
//            throw new BusinessException(ErrorCode.CHALLENGE_ALREADY_REVIEWED);
//        }
        challenge.setReviewStatus(ReviewStatus.REJECTED);
    }
}
