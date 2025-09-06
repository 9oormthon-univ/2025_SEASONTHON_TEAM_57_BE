package ONDA.domain.challenge.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChallengePostCalendarResponse {
    private int year;
    private int month;
    private List<DayPosts> days;

    @Getter @Setter
    @AllArgsConstructor
    public static class DayPosts {
        private LocalDate date;
        private int totalCont;
        private List<ChallengePostResponse> items;
    }
}
