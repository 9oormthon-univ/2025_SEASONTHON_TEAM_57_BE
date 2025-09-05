package ONDA.domain.challenge.service.impl;

import ONDA.domain.challenge.entity.Challenge;
import ONDA.domain.challenge.entity.ChallengePost;
import ONDA.domain.challenge.entity.ChallengeVote;
import ONDA.domain.challenge.dto.VoteResultResponse;
import ONDA.domain.challenge.repository.ChallengeRepository;
import ONDA.domain.challenge.repository.ChallengeVoteRepository;
import ONDA.domain.challenge.service.inf.ChallengeVoteService;
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

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeVoteServiceImpl implements ChallengeVoteService {
    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeVoteRepository challengeVoteRepository;

    @Override
    public void voteForParticipant(Long voterId, Long challengeId, Long participantId) {
        Member voter = memberRepository.findById(voterId)
                .orElseThrow(NotFoundMemberException::new);

        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_CHALLENGE_FOUND));

        Member participant = memberRepository.findById(participantId)
                .orElseThrow(NotFoundMemberException::new);

        // 중복 투표 방지
        if (challengeVoteRepository.existsByChallengeAndVoter(challenge, voter)) {
            throw new BusinessException(ErrorCode.ALREADY_VOTED);
        }

        ChallengeVote vote = ChallengeVote.builder()
                .challenge(challenge)
                .voter(voter)
                .participant(participant)
                .build();

        challengeVoteRepository.save(vote);
    }

    @Override
    public ApiResponse<List<VoteResultResponse>> getVoteResults(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_CHALLENGE_FOUND));

        List<Member> participants = challenge.getChallengePosts().stream()
                .map(ChallengePost::getAuthor)
                .distinct()
                .toList();

        List<VoteResultResponse> results = participants.stream()
                .map(p -> new VoteResultResponse(
                        p.getId(),
                        p.getNickname(),
                        p.getProfile(),
                        challengeVoteRepository.countByChallengeAndParticipant(challenge, p),
                        0
                ))
                .sorted(Comparator.comparingLong(VoteResultResponse::getVoteCount).reversed())
                .toList();

        AtomicInteger rankCounter = new AtomicInteger(1);
        List<VoteResultResponse> rankedResults = results.stream()
                .map(r -> new VoteResultResponse(
                        r.getParticipantId(),
                        r.getParticipantNickname(),
                        r.getParticipantProfile(),
                        r.getVoteCount(),
                        rankCounter.getAndIncrement()
                ))
                .toList();

        return ApiResponse.success(ResponseCode.SUCCESS, rankedResults);
    }
}
