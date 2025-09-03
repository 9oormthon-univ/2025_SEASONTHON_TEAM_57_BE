package ONDA.auth.infra.oauth.dto;

import ONDA.domain.member.entity.Gender;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class  TemporaryMemberInfo {
    private Long kakaoId;
    private boolean isDefaultProfile;
    private String profile;
    private Gender gender;
}