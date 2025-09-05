package ONDA.domain.member.dto;

import ONDA.domain.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberResponse {
    @Schema(description = "닉네임", example = "홍길동")
    private String nickName;

    public MemberResponse(Member member) {
        this.nickName = member.getNickname();
    }
}
