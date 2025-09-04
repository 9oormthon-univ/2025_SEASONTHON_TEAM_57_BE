package ONDA.domain.challenge.service.impl;

import ONDA.domain.challenge.dto.ChallengePostRequest;
import ONDA.domain.challenge.dto.ChallengePostResponse;
import ONDA.domain.challenge.dto.ChallengeResponse;
import ONDA.domain.challenge.entity.Challenge;
import ONDA.domain.challenge.entity.ChallengeCategory;
import ONDA.domain.challenge.entity.ChallengePost;
import ONDA.domain.challenge.entity.ChallengePostImage;
import ONDA.domain.challenge.repository.ChallengePostRepository;
import ONDA.domain.challenge.repository.ChallengeRepository;
import ONDA.domain.challenge.service.inf.ChallengePostService;
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

        Challenge challenge = challengeRepository.findById(dto.getChallengeId())
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
}
