package ONDA.auth.infra.oauth.dto;

import ONDA.domain.member.entity.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class  TemporaryMemberInfo {
    private Long kakaoId;
    private boolean isDefaultProfile;
    private String profile;
    private Gender gender;
}