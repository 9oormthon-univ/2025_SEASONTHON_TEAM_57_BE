package ONDA.auth.dto;

import ONDA.domain.member.entity.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
public class SignupRequest {
    private String linkToken;
    private String profile;
    private String realName;
    private String nickname;
    private Gender gender;
    private LocalDate birthDate;
}
