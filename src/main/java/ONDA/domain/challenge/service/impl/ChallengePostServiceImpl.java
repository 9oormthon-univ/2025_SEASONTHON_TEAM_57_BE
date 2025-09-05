package ONDA.domain.challenge.service.impl;

import ONDA.domain.challenge.dto.ChallengePostRequest;
import ONDA.domain.challenge.dto.ChallengePostResponse;
import ONDA.domain.challenge.dto.ChallengeResponse;
import ONDA.domain.challenge.entity.*;
import ONDA.domain.challenge.repository.ChallengePostRepository;
import ONDA.domain.challenge.repository.ChallengeRepository;
import ONDA.domain.challenge.service.inf.ChallengePostService;
import ONDA.domain.member.dto.MemberResponse;
import ONDA.domain.member.entity.Member;
import ONDA.domain.member.repository.MemberRepository;
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
public class ChallengePostServiceImpl implements ChallengePostService {
    private final MemberRepository memberRepository;
    private final ChallengePostRepository challengePostRepository;
    private final ChallengeRepository challengeRepository;

    @Override
    public void saveChallengePost(Long memberId, ChallengePostRequest dto){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        Challenge challenge = challengeRepository.findByIdAndProgressStatus(dto.getChallengeId(), ProgressStatus.ONGOING)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_CHALLENGE_FOUND));

        ChallengePost challengePost = ChallengePost.builder()
                        .author(member)
                        .challenge(challenge)
                        .createDate(LocalDate.now())
                        .build();

        List<ChallengePostImage> postImages = dto.getImages().stream()
                .map(imageUrl -> ChallengePostImage.of(challengePost, imageUrl))
                .toList();

        challengePost.setImages(postImages);
        challengePostRepository.save(challengePost);
    }

    @Override
    public ApiResponse<ChallengePostResponse> getChallengePost(Long challengePostId){
        ChallengePost challengePost = challengePostRepository.findById(challengePostId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_CHALLENGE_POST_FOUND));

        ChallengePostResponse challengePostResponse = new ChallengePostResponse(challengePost);

        return ApiResponse.success(ResponseCode.SUCCESS, challengePostResponse);
    }

    @Override
    public ApiResponse<List<ChallengePostResponse>> getMyChallengePostsByChallenge(Long memberId, Long challengeId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_CHALLENGE_FOUND));

        List<ChallengePostResponse> challengePosts= challengePostRepository.findByAuthorAndChallenge(member,challenge).stream()
                .map(ChallengePostResponse::new)
                .toList();

        return ApiResponse.success(ResponseCode.SUCCESS, challengePosts);
    }

    @Override
    public ApiResponse<List<ChallengePostResponse>> getMyChallengePostsByDate(Long memberId, LocalDate targetDate) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        List<ChallengePostResponse> challengePosts= challengePostRepository.findByAuthorAndCreateDate(member,targetDate).stream()
                .map(ChallengePostResponse::new)
                .toList();

        return ApiResponse.success(ResponseCode.SUCCESS, challengePosts);
    }

    @Override
    public ApiResponse<List<MemberResponse>> getChallengeParticipants(Long challengeId) {
        List<Member> authors = challengePostRepository.findDistinctAuthorsByChallengeId(challengeId);

        List<MemberResponse> participants = authors.stream()
                .map(MemberResponse::new)
                .toList();

        return ApiResponse.success(ResponseCode.SUCCESS, participants);
    }

    @Override
    public ApiResponse<List<ChallengePostResponse>> getPostsByChallengeAndMember(Long challengeId, Long memberId) {
        List<ChallengePostResponse> responses = challengePostRepository
                .findByChallengeIdAndAuthorId(challengeId, memberId).stream()
                .map(ChallengePostResponse::new)
                .toList();

        return ApiResponse.success(ResponseCode.SUCCESS, responses);
    }
}
