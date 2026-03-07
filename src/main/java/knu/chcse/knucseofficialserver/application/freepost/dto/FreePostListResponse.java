package knu.chcse.knucseofficialserver.application.freepost.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class FreePostListResponse {
    private List<FreePostResponse> pinnedQuestions;

    private Page<FreePostResponse> posts;
}