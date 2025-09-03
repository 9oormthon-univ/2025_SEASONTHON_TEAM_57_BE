package ONDA.domain.challenge.service.impl;

import ONDA.domain.challenge.dto.ChallengeRequestDto;
import ONDA.domain.challenge.entity.Challenge;
import ONDA.domain.challenge.repository.ChallengeRepository;
import ONDA.domain.challenge.service.inf.ChallengeService;
import ONDA.domain.member.entity.Member;
import ONDA.domain.member.repository.MemberRepository;
import ONDA.global.exception.NotFoundMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final MemberRepository memberRepository;

    @Override
    public void saveChallenge(Long memberId, ChallengeRequestDto dto){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        Challenge challenge = dto.toEntity(member);
        challengeRepository.save(challenge);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChallengeRequestDto> getAllChallenges(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        return challengeRepository.findAll().stream()
                .map(ChallengeRequestDto::new)
                .toList();
    }
}
