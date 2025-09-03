package ONDA.auth.service;

import ONDA.auth.dto.AccessTokenResponse;
import ONDA.auth.dto.LoginResponse;
import ONDA.auth.dto.SignupRequest;
import ONDA.auth.infra.jwt.JwtAuthFilter;
import ONDA.auth.infra.jwt.JwtProvider;
import ONDA.auth.infra.jwt.TokenClaims;
import ONDA.auth.infra.oauth.KakaoOAuthClient;
import ONDA.auth.infra.oauth.dto.TemporaryMemberInfo;
import ONDA.auth.infra.redis.RedisService;
import ONDA.domain.member.entity.Member;
import ONDA.domain.member.entity.Role;
import ONDA.domain.member.service.MemberService;
import ONDA.global.exception.BusinessException;
import ONDA.global.exception.ErrorCode;
import ONDA.global.response.ApiResponse;
import ONDA.global.response.ResponseCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final KakaoOAuthClient kakaoOAuthClient;
    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final JwtAuthFilter jwtAuthFilter;
    private final RedisService redisService;

    @Value("${jwt.access-exp-seconds}")
    private Long REFRESH_TOKEN_EXPIRE_TIME;

    @Value("${jwt.refresh-exp-seconds}")
    private Long ACCESS_TOKEN_EXPIRE_TIME;



    public ApiResponse<LoginResponse> authenticateKakaoUser(String code) {
        String accessTokenFromKakao = kakaoOAuthClient.getAccessTokenFromKakao(code);
        TemporaryMemberInfo memberInfo = kakaoOAuthClient.getUserInfo(accessTokenFromKakao);

        Long kakaoId = memberInfo.getKakaoId();

        Optional<Member> optionalMember = memberService.findMember(kakaoId);
        if (optionalMember.isPresent()) { //기존 회원
            Member member = optionalMember.get();
            Long memberId = member.getId();
            Role role = member.getRole();

            String accessToken = jwtProvider.generateAccessToken(memberId, role);
            String refreshToken = jwtProvider.generateRefreshToken(memberId);

            redisService.setData("refresh:" + memberId, refreshToken, REFRESH_TOKEN_EXPIRE_TIME);
            return ApiResponse.success(ResponseCode.AUTH_LOGIN_SUCCESS, new LoginResponse(null,null, accessToken, refreshToken));
        } else { //신규 회원 (Redis로 임시 세션 생성)
            String linkToken = UUID.randomUUID().toString();
            redisService.setData("temp-oauth:" + linkToken, kakaoId, ACCESS_TOKEN_EXPIRE_TIME);
            memberInfo.setKakaoId(null);
            return ApiResponse.success(ResponseCode.AUTH_SIGNUP_REQUIRED, new LoginResponse(linkToken, memberInfo, null, null));
        }
    }


    public ApiResponse<LoginResponse> signup(SignupRequest request) {
        String linkToken = request.getLinkToken();
        String key = "temp-oauth:" + linkToken;

        Long kakaoId = (Long) redisService.getData(key);

        if (kakaoId == null) {
            throw new BusinessException(ErrorCode.SIGNUP_SESSION_EXPIRED);
        }

        Member member = Member.builder()
                .kakaoId(kakaoId)
                .profile(request.getProfile())
                .realName(request.getRealName())
                .nickname(request.getNickname())
                .gender(request.getGender())
                .birthDate(request.getBirthDate())
                .build();

        memberService.save(member);

        Long memberId = member.getId();

        String accessToken = jwtProvider.generateAccessToken(memberId, Role.ROLE_USER);
        String refreshToken = jwtProvider.generateRefreshToken(memberId);

        redisService.deleteData(key);
        redisService.setData("refresh:" + memberId, refreshToken, REFRESH_TOKEN_EXPIRE_TIME);
        return ApiResponse.success(ResponseCode.AUTH_LOGIN_SUCCESS, new LoginResponse(null,null, accessToken, refreshToken));
    }

    public void logout(String accessToken, String refreshToken) {
        Long memberId;

        accessToken = jwtProvider.resolveToken(accessToken);
        if (accessToken == null) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        try {
            TokenClaims claims = jwtProvider.parseAndValidate(accessToken, "access");
            String tokenId = claims.getJti();

            //블랙리스트 등록
            redisService.setData("blacklist:access:" + tokenId, "logout", ACCESS_TOKEN_EXPIRE_TIME);
            memberId = claims.getUserId();
        } catch (ExpiredJwtException e) { //토큰 만료
            Claims claims = e.getClaims();
            memberId = Long.valueOf(claims.getSubject());
        }

        //리프레시 토큰 삭제
        validateRefreshToken(memberId, refreshToken);
        redisService.deleteData("refresh:" + memberId);
    }

    public AccessTokenResponse reissueAccessToken(String refreshToken) {
        TokenClaims claims = jwtProvider.parseAndValidate(refreshToken, "refresh");

        Long memberId = claims.getUserId();

        validateRefreshToken(memberId, refreshToken);

        Optional<Member> member = memberService.findMember(memberId);
        Role role = member
                .orElseThrow(() -> new BusinessException(ErrorCode.BAD_REQUEST))
                .getRole();
        String newAccessToken = jwtProvider.generateAccessToken(memberId, role);

        return new AccessTokenResponse(newAccessToken);
    }

    private void validateRefreshToken(Long memberId, String refreshToken) {
        String savedRefresh = String.valueOf(redisService.getData("refresh:" + memberId));
        if (savedRefresh == null) {
            throw new BusinessException(ErrorCode.EXPIRED_TOKEN);
        } else if (!refreshToken.equals(savedRefresh)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }
    }
}
