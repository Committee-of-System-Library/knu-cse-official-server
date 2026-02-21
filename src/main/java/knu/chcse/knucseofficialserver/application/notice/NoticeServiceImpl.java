package knu.chcse.knucseofficialserver.application.notice;

import knu.chcse.knucseofficialserver.application.notice.dto.CreateNoticeRequest;
import knu.chcse.knucseofficialserver.application.notice.dto.NoticeResponse;
import knu.chcse.knucseofficialserver.application.notice.dto.UpdateNoticeRequest;
import knu.chcse.knucseofficialserver.domain.entity.board.Board;
import knu.chcse.knucseofficialserver.domain.entity.board.BoardCategory;
import knu.chcse.knucseofficialserver.domain.entity.board.BoardJpaRepository;
import knu.chcse.knucseofficialserver.domain.entity.post.Post;
import knu.chcse.knucseofficialserver.domain.entity.post.PostJpaRepository;
import knu.chcse.knucseofficialserver.domain.entity.post.PostStatus;
import knu.chcse.knucseofficialserver.domain.entity.student.Student;
import knu.chcse.knucseofficialserver.domain.entity.student.StudentJpaRepository;
import knu.chcse.knucseofficialserver.domain.entity.student.StudentRole;
import knu.chcse.knucseofficialserver.global.error.CommonErrorCode;
import knu.chcse.knucseofficialserver.global.error.NoticeErrorCode;
import knu.chcse.knucseofficialserver.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeServiceImpl implements NoticeService {
    private final PostJpaRepository postRepository;
    private final StudentJpaRepository studentRepository;
    private final BoardJpaRepository boardRepository;

    @Override
    public Long createNotice(CreateNoticeRequest request, Long studentNumber) {
        Student student = checkAdminPermission(studentNumber);

        Board noticeBoard = boardRepository.findByCategory(BoardCategory.NOTICE).orElseThrow(
            ()-> new BusinessException(CommonErrorCode.NOT_FOUND)
        );

        Post post = Post.create(student, noticeBoard, request.title(), request.content(), false);

        postRepository.save(post);

        return post.getId();
    }

    @Override
    @Transactional
    public NoticeResponse getNotice(Long noticeId) {
        Post post = getNoticePost(noticeId);

        //soft delete check
        if(post.getStatus() != PostStatus.ACTIVE){
            throw new BusinessException(CommonErrorCode.NOT_FOUND);
        }

        // 조회수 증가
        post.incrementViewCount();

        return NoticeResponse.from(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoticeResponse> getNotices() {
        return postRepository
            .findByBoard_CategoryAndStatusOrderByCreatedAtDesc(
                BoardCategory.NOTICE,
                PostStatus.ACTIVE
            )
            .stream()
            .map(NoticeResponse::from)
            .toList();
    }

    @Override
    public void updateNotice(Long noticeId, Long studentNumber, UpdateNoticeRequest request) {
        Post post = getNoticePost(noticeId);
        if (post.getStatus() != PostStatus.ACTIVE) {
            throw new BusinessException(CommonErrorCode.NOT_FOUND);
        }

        checkAdminPermission(studentNumber);

        post.update(request.title(), request.content());
    }

    @Override
    public void deleteNotice(Long noticeId, Long studentNumber) {
        Post post = getNoticePost(noticeId);
        if (post.getStatus() != PostStatus.ACTIVE) {
            throw new BusinessException(CommonErrorCode.NOT_FOUND);
        }

        checkAdminPermission(studentNumber);

        post.delete();
    }

    private Student checkAdminPermission(Long studentNumber){
        Student student = studentRepository.findByNumber(studentNumber).orElseThrow(
            ()-> new BusinessException(CommonErrorCode.INVALID_CREDENTIALS)
        );

        if(student.getRole() != StudentRole.ADMIN){
            throw new BusinessException(NoticeErrorCode.NO_NOTICE_PERMISSION);
        }

        return student;
    }

    private Post getNoticePost(Long noticeId){
        Post post = postRepository.findById(noticeId).orElseThrow(
            ()-> new BusinessException(CommonErrorCode.NOT_FOUND)
        );

        if(!post.isNotice()){
            throw new BusinessException(NoticeErrorCode.NOT_NOTICE);
        }

        return post;
    }
}