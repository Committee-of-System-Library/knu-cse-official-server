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

import java.util.ArrayList;
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

        checkAdminPermission(studentNumber);

        //실제 객체 생성을 위해 checkAdminPermission 사용 후에 객체 생성
        Student student = studentRepository.findByNumber(studentNumber).orElseThrow(
                ()-> new BusinessException(CommonErrorCode.NOT_FOUND)
        );

        Board noticeBoard = boardRepository.findByCategory(BoardCategory.NOTICE).orElseThrow(
                ()-> new BusinessException(CommonErrorCode.NOT_FOUND)
        );

        Post post = Post.create(student,noticeBoard,request.getTitle(),request.getContent(),false);

        postRepository.save(post);

        return post.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public NoticeResponse getNotice(Long noticeId) {
         Post post = getNoticePost(noticeId);

         //soft delete check
         if(post.getStatus() != PostStatus.ACTIVE){
             throw new BusinessException(CommonErrorCode.NOT_FOUND);
         }

         return NoticeResponse.from(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoticeResponse> getNotices() {
        List<Post> posts = postRepository.findByNoticeTrueAndStatusOrderByCreatedAtDesc(PostStatus.ACTIVE);

        List<NoticeResponse> notices = new ArrayList<>();

        for(Post post : posts){
            NoticeResponse notice = NoticeResponse.from(post);
            notices.add(notice);
        }

        return notices;
    }

    @Override
    public void updateNotice(Long noticeId, Long studentNumber, UpdateNoticeRequest request) {
        checkAdminPermission(studentNumber);

        Post post = getNoticePost(noticeId);

        post.update(request.getTitle(),request.getContent());
    }

    @Override
    public void deleteNotice(Long noticeId, Long studentNumber) {

        checkAdminPermission(studentNumber);

        Post post = getNoticePost(noticeId);

        post.delete();
    }

    //duplicate code remove
    private void checkAdminPermission(Long studentNumber){
        Student student = studentRepository.findByNumber(studentNumber).orElseThrow(
                ()-> new BusinessException(CommonErrorCode.NOT_FOUND)
        );

        //permission check
        if(student.getRole() != StudentRole.ADMIN){
            throw new BusinessException(NoticeErrorCode.NO_NOTICE_PERMISSION);
        }
    }

    private Post getNoticePost(Long noticeId){
        Post post = postRepository.findById(noticeId).orElseThrow(
                ()-> new BusinessException(CommonErrorCode.NOT_FOUND)
        );

        //lombok made isNotice , not "getIsNotice"
        //notice check
        if(!post.isNotice()){
            throw new BusinessException(NoticeErrorCode.NOT_NOTICE);
        }

        return post;
    }
}
