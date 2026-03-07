package knu.chcse.knucseofficialserver.application.freepost;

import knu.chcse.knucseofficialserver.application.freepost.dto.CreateFreePostRequest;
import knu.chcse.knucseofficialserver.application.freepost.dto.FreePostListResponse;
import knu.chcse.knucseofficialserver.application.freepost.dto.FreePostResponse;
import knu.chcse.knucseofficialserver.application.freepost.dto.UpdateFreePostRequest;
import org.springframework.data.domain.Pageable;

public interface FreePostService {
    Long createFreePost(CreateFreePostRequest request, Long studentNumber);
    FreePostResponse getFreePost(Long postId);
    FreePostListResponse getFreePosts(Pageable pageable);
    void updateFreePost(Long postId, Long studentNumber, UpdateFreePostRequest request);
    void deleteFreePost(Long postId, Long studentNumber);
}