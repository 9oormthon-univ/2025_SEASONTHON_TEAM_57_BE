package ONDA.domain.member.service;

import ONDA.domain.member.entity.Member;
import ONDA.domain.member.repository.MemberRepository;
import ONDA.global.exception.BusinessException;
import ONDA.global.exception.NotFoundMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Optional<Member> findMember(Long kakaoId) {
        return memberRepository.findByKakaoId(kakaoId);
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);
    }

    public void save(Member member) {
        memberRepository.save(member);
    }

    public void uploadProfile() {

    }
//    @Transactional(readOnly = true)
//    public MemberResponse findMemberById(Long memberId) {
//        return MemberResponse.from(getMember(memberId));
//    }
}
