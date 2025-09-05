package ONDA.domain.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import java.time.LocalDate;

import static ONDA.domain.member.entity.Role.ROLE_USER;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String profile;

    private String realName;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;

    private Long kakaoId;

    @Enumerated(EnumType.STRING)
    private Role role = ROLE_USER;

    @Builder
    private Member(String profile, String realName, String nickname, Gender gender,
                   LocalDate birthDate, Long kakaoId) {
        this.profile = profile;
        this.realName = realName;
        this.nickname = nickname;
        this.gender = gender;
        this.birthDate = birthDate;
        this.kakaoId = kakaoId;
    }
}
