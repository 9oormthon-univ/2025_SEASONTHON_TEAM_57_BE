package ONDA.auth.infra.oauth.dto;

import ONDA.domain.member.entity.Gender;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class  TemporaryMemberInfo {
    private Long kakaoId;
    private boolean isDefaultProfile;
    private String profile;
    private Gender gender;

    public TemporaryMemberInfo(Long kakaoId, boolean isDefaultProfile, String profile, String gender) {
        this.kakaoId = kakaoId;
        this.isDefaultProfile = isDefaultProfile;
        this.profile = profile;
        if (gender != null) {
            this.gender = Gender.valueOf(gender.toUpperCase());
        }
    }

    public TemporaryMemberInfo(Long kakaoId) {
        this(kakaoId, true, null, null);
    }
}