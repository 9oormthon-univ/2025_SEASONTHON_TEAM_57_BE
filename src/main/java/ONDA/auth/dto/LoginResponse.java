package ONDA.auth.dto;

import ONDA.auth.infra.oauth.dto.TemporaryMemberInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    private final String linkToken; //회원가입용 임시 세션
    private final TemporaryMemberInfo memberInfo;
    private final String accessToken;
    private final String refreshToken;
}
