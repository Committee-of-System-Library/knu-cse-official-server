package knu.chcse.knucseofficialserver.application.freepost;

import knu.chcse.knucseofficialserver.application.freepost.dto.CreateFreePostRequest;
import knu.chcse.knucseofficialserver.application.freepost.dto.FreePostListResponse;
import knu.chcse.knucseofficialserver.application.freepost.dto.FreePostResponse;
import knu.chcse.knucseofficialserver.application.freepost.dto.UpdateFreePostRequest;
import knu.chcse.knucseofficialserver.domain.entity.board.Board;
import knu.chcse.knucseofficialserver.domain.entity.board.BoardCategory;
import knu.chcse.knucseofficialserver.domain.entity.board.BoardJpaRepository;
import knu.chcse.knucseofficialserver.domain.entity.comment.CommentJpaRepository;
import knu.chcse.knucseofficialserver.domain.entity.post.Post;
import knu.chcse.knucseofficialserver.domain.entity.post.PostJpaRepository;
import knu.chcse.knucseofficialserver.domain.entity.post.PostStatus;
import knu.chcse.knucseofficialserver.domain.entity.student.Student;
import knu.chcse.knucseofficialserver.domain.entity.student.StudentJpaRepository;
import knu.chcse.knucseofficialserver.global.error.CommonErrorCode;
import knu.chcse.knucseofficialserver.global.error.FreePostErrorCode;
import knu.chcse.knucseofficialserver.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FreePostServiceImpl implements FreePostService {

    private final PostJpaRepository postRepository;
    private final StudentJpaRepository studentRepository;
    private final BoardJpaRepository boardRepository;
    private final CommentJpaRepository commentRepository;

    @Override
    public Long createFreePost(CreateFreePostRequest request, Long studentNumber) {
        Student student = findStudentByNumber(studentNumber);

        Board freeBoard = boardRepository.findByCategory(BoardCategory.FREE).orElseThrow(
            () -> new BusinessException(CommonErrorCode.NOT_FOUND)
        );

        Post post = Post.create(
            student,
            freeBoard,
            request.title(),
            request.content(),
            request.anonymous(),
            request.question()
        );
        postRepository.save(post);

        return post.getId();
    }

    @Override
    public FreePostResponse getFreePost(Long postId) {
        Post post = getActiveFreePost(postId);
        post.incrementViewCount();
        return FreePostResponse.from(post);
    }

    @Override
    @Transactional(readOnly = true)
    public FreePostListResponse getFreePosts(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();

        // 현재 고정 중인 활성 질문글 최신순 최대 5개
        List<FreePostResponse> pinnedQuestions = postRepository
            .findTop5ByBoard_CategoryAndStatusAndQuestionTrueAndPinnedUntilAfterOrderByCreatedAtDesc(
                BoardCategory.FREE,
                PostStatus.ACTIVE,
                now
            )
            .stream()
            .map(FreePostResponse::from)
            .toList();

        // 전체 활성 게시글 최신순 페이징
        Page<FreePostResponse> posts = postRepository
            .findByBoard_CategoryAndStatusOrderByCreatedAtDesc(
                BoardCategory.FREE,
                PostStatus.ACTIVE,
                pageable
            )
            .map(FreePostResponse::from);

        return new FreePostListResponse(pinnedQuestions, posts);
    }

    @Override
    public void updateFreePost(Long postId, Long studentNumber, UpdateFreePostRequest request) {
        Post post = getActiveFreePost(postId);
        validateOwnership(post, studentNumber);
        validateModifiable(post);
        post.update(request.title(), request.content());
    }

    @Override
    public void deleteFreePost(Long postId, Long studentNumber) {
        Post post = getActiveFreePost(postId);
        validateOwnership(post, studentNumber);
        validateModifiable(post);
        post.delete();
    }

    private Student findStudentByNumber(Long studentNumber) {
        return studentRepository.findByNumber(studentNumber).orElseThrow(
            () -> new BusinessException(CommonErrorCode.INVALID_CREDENTIALS)
        );
    }

    private Post getActiveFreePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
            () -> new BusinessException(CommonErrorCode.NOT_FOUND)
        );

        if (!post.isFreePost()) {
            throw new BusinessException(FreePostErrorCode.NOT_FREE_POST);
        }

        if (post.getStatus() != PostStatus.ACTIVE) {
            throw new BusinessException(CommonErrorCode.NOT_FOUND);
        }

        return post;
    }

    private void validateOwnership(Post post, Long studentNumber) {
        if (!post.isOwnedBy(studentNumber)) {
            throw new BusinessException(FreePostErrorCode.NO_FREE_POST_PERMISSION);
        }
    }

    private void validateModifiable(Post post) {
        boolean hasComments = commentRepository.existsByPostId(post.getId());
        if (!post.isModifiable(hasComments)) {
            throw new BusinessException(FreePostErrorCode.CANNOT_MODIFY_COMMENTED_QUESTION);
        }
    }
}