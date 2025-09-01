package ONDA.auth.infra.oauth;

import ONDA.auth.infra.oauth.dto.KakaoTokenResponse;
import ONDA.auth.infra.oauth.dto.KakaoMemberInfoResponse;
import ONDA.auth.infra.oauth.dto.TemporaryMemberInfo;
import ONDA.domain.member.entity.Gender;
import ONDA.global.exception.BusinessException;
import ONDA.global.exception.ErrorCode;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import ONDA.auth.infra.oauth.dto.KakaoMemberInfoResponse.KakaoAccount.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class KakaoOAuthClient {
    @Value("${kakao.client_id}")
    private String clientId;

    private static final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com";
    private static final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com";

    public String getAccessTokenFromKakao(String code) {
        KakaoTokenResponse kakaoTokenResponseDto = WebClient.create(KAUTH_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new BusinessException(ErrorCode.KAKAO_INVALID_TOKEN)))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new BusinessException(ErrorCode.KAKAO_INTERNAL_ERROR)))
                .bodyToMono(KakaoTokenResponse.class)
                .block();

        return kakaoTokenResponseDto.getAccessToken();
    }

    public TemporaryMemberInfo getUserInfo(String accessToken) {
        KakaoMemberInfoResponse userInfo = WebClient.create(KAUTH_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new BusinessException(ErrorCode.KAKAO_INVALID_TOKEN)))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new BusinessException(ErrorCode.KAKAO_INTERNAL_ERROR)))
                .bodyToMono(KakaoMemberInfoResponse.class)
                .block();

        Profile profile = userInfo.getKakaoAccount().getProfile();

        return new TemporaryMemberInfo(userInfo.getId(), profile.getIsDefaultImage(), profile.getProfileImageUrl(), userInfo.getKakaoAccount().getGender());
    }
}
