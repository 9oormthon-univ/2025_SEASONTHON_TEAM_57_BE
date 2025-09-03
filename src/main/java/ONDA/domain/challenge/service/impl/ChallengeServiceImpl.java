package ONDA.domain.challenge.service.impl;

import ONDA.domain.challenge.dto.ChallengeRequest;
import ONDA.domain.challenge.dto.ChallengeResponse;
import ONDA.domain.challenge.entity.Challenge;
import ONDA.domain.challenge.entity.ChallengeCategory;
import ONDA.domain.challenge.repository.ChallengeRepository;
import ONDA.domain.challenge.service.inf.ChallengeService;
import ONDA.domain.member.entity.Member;
import ONDA.domain.member.repository.MemberRepository;
import ONDA.global.category.Category;
import ONDA.global.category.CategoryRepository;
import ONDA.global.exception.NotFoundMemberException;
import ONDA.global.response.ApiResponse;
import ONDA.global.response.ResponseCode;
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
    private final CategoryRepository categoryRepository;

    @Override
    public void saveChallenge(Long memberId, ChallengeRequest dto){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

//        for (Long categoryId : dto.getCategoryIds()) {
//            Category category = categoryRepository.findById(categoryId)
//                    .orElseThrow(NotFoundMemberException::new);
//            ChallengeCategory challengeCategory = new ChallengeCategory();
//            challengeCategory.setCategory(category);
//        }
        List<Category> categories = categoryRepository.findAllById(dto.getCategoryIds());

        Challenge challenge = dto.toEntity(member,categories);
        challengeRepository.save(challenge);
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse<List<ChallengeResponse>> getAllChallenges() {
        List<ChallengeResponse> challenges= challengeRepository.findAll().stream()
                .map(ChallengeResponse::new)
                .toList();

        return ApiResponse.success(ResponseCode.SUCCESS, challenges);
    }
}
