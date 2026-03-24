package knu.chcse.knucseofficialserver.application.notice;

import knu.chcse.knucseofficialserver.application.notice.dto.*;
import knu.chcse.knucseofficialserver.domain.entity.notice.Notice;
import knu.chcse.knucseofficialserver.domain.entity.board.Board;
import knu.chcse.knucseofficialserver.domain.entity.board.BoardCategory;
import knu.chcse.knucseofficialserver.domain.entity.board.BoardJpaRepository;
import knu.chcse.knucseofficialserver.domain.entity.notice.NoticeRepository;
import knu.chcse.knucseofficialserver.domain.entity.post.Post;
import knu.chcse.knucseofficialserver.domain.entity.post.PostJpaRepository;
import knu.chcse.knucseofficialserver.domain.entity.post.PostStatus;
import knu.chcse.knucseofficialserver.domain.entity.student.Student;
import knu.chcse.knucseofficialserver.domain.entity.student.StudentJpaRepository;
import knu.chcse.knucseofficialserver.domain.entity.student.StudentRole;
import knu.chcse.knucseofficialserver.global.error.CommonErrorCode;
import knu.chcse.knucseofficialserver.global.error.NoticeErrorCode;
import knu.chcse.knucseofficialserver.global.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NoticeServiceImpl 테스트")
public class NoticeServiceImplTest {

    @Mock
    private PostJpaRepository postJpaRepository;
    @Mock
    private StudentJpaRepository studentJpaRepository;
    @Mock
    private BoardJpaRepository boardJpaRepository;
    @Mock
    private NoticeRepository noticeRepository;

    private NoticeServiceImpl noticeService;

    @BeforeEach
    void setUp(){
        this.noticeService = new NoticeServiceImpl(noticeRepository,postJpaRepository, studentJpaRepository, boardJpaRepository);
    }

    @Test
    @DisplayName("createNotice: 성공")
    void createNoticeSuccess(){
        //given
        Long studentNumber = 20261234L;
        CreateNoticeRequest request = new CreateNoticeRequest("title","content");
        Student admin = givenAdmin(studentNumber);
        Board noticeBoard = mock(Board.class);
        when(boardJpaRepository.findByCategory(BoardCategory.NOTICE)).thenReturn(Optional.of(noticeBoard));

        //when
        Long id = noticeService.createNotice(request,studentNumber);

        //then
        verify(postJpaRepository).save(any(Post.class));

    }


    @Test
    @DisplayName("createNotice: 학생이 없으면 INVALID_CREDENTIALS exception")
    void createNotice_fail_studentNotfound(){
        //given
        Long studentNumber = 20261234L;
        CreateNoticeRequest request = new CreateNoticeRequest("title","content");
        when(studentJpaRepository.findByNumber(studentNumber)).thenReturn(Optional.empty());

        //when
        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> noticeService.createNotice(request,studentNumber)
        );

        //then
        assertEquals(CommonErrorCode.INVALID_CREDENTIALS, ex.getErrorCode());
    }

    @Test
    @DisplayName("createNotice: ADMIN이 아니면 NO_NOTICE_PERMISSION exception")
    void createNotice_fail_notAdmin(){
        // given
        Long studentNumber = 20261234L;
        CreateNoticeRequest request = new CreateNoticeRequest("title", "content");
        Student student = givenStudent(studentNumber);

        // when
        BusinessException ex = assertThrows(BusinessException.class,
                () -> noticeService.createNotice(request, studentNumber));

        // then
        assertEquals(NoticeErrorCode.NO_NOTICE_PERMISSION, ex.getErrorCode());
    }

    @Test
    @DisplayName("createNotice: NOTICE 보드가 없으면 NOT_FOUND 예외")
    void createNotice_fail_noticeBoardNotFound(){
        // given
        Long studentNumber = 20261234L;
        CreateNoticeRequest request = new CreateNoticeRequest("title", "content");

        Student admin = givenAdmin(studentNumber);
        when(boardJpaRepository.findByCategory(BoardCategory.NOTICE)).thenReturn(Optional.empty());

        // when
        BusinessException ex = assertThrows(BusinessException.class,
                () -> noticeService.createNotice(request, studentNumber));

        // then
        assertEquals(CommonErrorCode.NOT_FOUND, ex.getErrorCode());
    }


    @Test
    @DisplayName("getNotice: 성공")
    void getNotice_success(){
        //given
        Long noticeId = 1L;
        Post post = givenNoticePost(noticeId);
        when(post.getTitle()).thenReturn("title");
        when(post.getContent()).thenReturn("content");

        //when
        NoticeResponse response = noticeService.getNotice(noticeId);

        //then
        assertAll(
                () -> assertEquals("title",response.getTitle()),
                () -> assertEquals("content",response.getContent())
        );
        verify(post).incrementViewCount();
    }

    @Test
    @DisplayName("getNotice : 삭제된 글이면 NOT_FOUND")
    void getNotice_fail_noticeBoardNotFound(){
        //given
        Long noticeId = 1L;
        Post post = givenDeletedNoticePost(noticeId);

        //when
        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> noticeService.getNotice(noticeId)
        );

        //then
        assertEquals(CommonErrorCode.NOT_FOUND, ex.getErrorCode());
    }

    @Test
    @DisplayName("getNotice : post 없는 경우 NOT_FOUND")
    void getNotice_fail_postNotfound(){
        //given
        Long noticeId = 1L;
        when(postJpaRepository.findById(noticeId)).thenReturn(Optional.empty());

        //when
        BusinessException ex = assertThrows(
                BusinessException.class,()-> noticeService.getNotice(noticeId)
        );

        //then
        assertEquals(CommonErrorCode.NOT_FOUND, ex.getErrorCode());
    }

    @Test
    @DisplayName("getNotice : notice 글이 아니면 NOT_NOTICE")
    void getNotice_not_notice(){

        //given
        Long noticeId = 1L;
        Post post = mock(Post.class);

        when(postJpaRepository.findById(noticeId)).thenReturn(Optional.of(post));
        when(post.isNotice()).thenReturn(false);
        //when
        BusinessException ex = assertThrows(
                BusinessException.class,()-> noticeService.getNotice(noticeId)
        );

        //then
        assertEquals(NoticeErrorCode.NOT_NOTICE, ex.getErrorCode());
    }


    @Test
    @DisplayName("updateNotice : 성공")
    void updateNotice_success(){
        //given
        Long noticeId = 1L;
        Long studentNumber = 20261234L;

        UpdateNoticeRequest request = new UpdateNoticeRequest("update title", "update content");

        Post post = givenNoticePost(noticeId);
        Student admin = givenAdmin(studentNumber);

        //when
        noticeService.updateNotice(noticeId, studentNumber, request);

        //then
        verify(post).update(request.title(), request.content());
    }

    @Test
    @DisplayName("updateNotice : post 없으면 Not_FOUND")
    void updateNotice_fail_postNotFound(){
        //given
        Long noticeId = 1L;
        Long studentNumber = 20261234L;
        UpdateNoticeRequest request = new UpdateNoticeRequest("update title", "update content");

        when(postJpaRepository.findById(noticeId)).thenReturn(Optional.empty());

        //when
        BusinessException ex = assertThrows(
                BusinessException.class,
                ()-> noticeService.updateNotice(noticeId, studentNumber, request)
        );

        //then
        assertEquals(CommonErrorCode.NOT_FOUND, ex.getErrorCode());
    }

    @Test
    @DisplayName("updateNotice: notice 글 아니면 NOT_NOTICE")
    void updateNotice_fail_noticeNotice(){
        //given
        Long noticeId = 1L;
        Long studentNumber = 20261234L;
        Post post = mock(Post.class);
        UpdateNoticeRequest request = new UpdateNoticeRequest("update title", "update content");

        when(postJpaRepository.findById(noticeId)).thenReturn(Optional.of(post));
        when(post.isNotice()).thenReturn(false);

        //when
        BusinessException ex = assertThrows(
                BusinessException.class,
                ()-> noticeService.updateNotice(noticeId, studentNumber, request)
        );

        //then
        assertEquals(NoticeErrorCode.NOT_NOTICE, ex.getErrorCode());
    }

    @Test
    @DisplayName("updateNotice: 삭제된 글이면 NOT_FOUND")
    void updateNotice_fail_deleted(){
        //given
        Long noticeId = 1L;
        Long studentNumber = 20261234L;
        UpdateNoticeRequest request = new UpdateNoticeRequest("update title", "update content");
        Post post = givenDeletedNoticePost(noticeId);

        //when
        BusinessException ex = assertThrows(
            BusinessException.class,
                ()-> noticeService.updateNotice(noticeId, studentNumber, request)
        );

        assertEquals(CommonErrorCode.NOT_FOUND, ex.getErrorCode());
    }

    @Test
    @DisplayName("updateNotice: ADMIN 아니라면 NO_NOTICE_PERMISSION")
    void updateNotice_fail_notAdmin(){
        //given
        Long noticeId = 1L;
        Long studentNumber = 20261234L;
        Student student = givenStudent(studentNumber);
        UpdateNoticeRequest request = new UpdateNoticeRequest("update title", "update content");
        Post post = givenActiveNoticePost(noticeId);

        //when
        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> noticeService.updateNotice(noticeId, studentNumber, request)
        );

        //then
        assertEquals(NoticeErrorCode.NO_NOTICE_PERMISSION, ex.getErrorCode());
    }

    @Test
    @DisplayName("deleteNotice: 성공")
    void deleteNotice_success(){
        //given
        Long noticeId = 1L;
        Long studentNumber = 20261234L;
        Post post = givenNoticePost(noticeId);
        Student admin = givenAdmin(studentNumber);

        //when
        noticeService.deleteNotice(noticeId, studentNumber);

        //then
        verify(post).delete();
    }

    @Test
    @DisplayName("deleteNotice: 이미 삭제된 글이면 NOT_FOUND")
    void deleteNotice_fail_deleted() {
        //given
        Long noticeId = 1L;
        Long studentNumber = 20261234L;
        Post post = givenDeletedNoticePost(noticeId);

        //when
        BusinessException ex = assertThrows(
                BusinessException.class,
                ()-> noticeService.deleteNotice(noticeId, studentNumber)
        );

        //then
        assertEquals(CommonErrorCode.NOT_FOUND, ex.getErrorCode());
    }

    @Test
    @DisplayName("deleteNotice: post 없으면 NOT_FOUND")
    void deleteNotice_fail_postNotFound() {
        //given
        Long noticeId = 1L;
        Long studentNumber = 20261234L;

        when(postJpaRepository.findById(noticeId)).thenReturn(Optional.empty());

        //when
        BusinessException ex = assertThrows(
                BusinessException.class,
                ()-> noticeService.deleteNotice(noticeId, studentNumber)
        );

        //then
        assertEquals(CommonErrorCode.NOT_FOUND, ex.getErrorCode());
    }

    @Test
    @DisplayName("deleteNotice: notice 게시글이 아니면 NOT_NOTICE")
    void deleteNotice_fail_notNotice(){
        //given
        Long noticeId = 1L;
        Long studentNumber = 20261234L;
        Post post = mock(Post.class);

        when(postJpaRepository.findById(noticeId)).thenReturn(Optional.of(post));
        when(post.isNotice()).thenReturn(false);

        //when
        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> noticeService.deleteNotice(noticeId, studentNumber)
        );

        //then
        assertEquals(NoticeErrorCode.NOT_NOTICE, ex.getErrorCode());
    }

    @Test
    @DisplayName("deleteNotice: student 없으면 INVALID_CREDENTIALS")
    void deleteNotice_fail_studentNotFound(){
        //given
        Long noticeId = 1L;
        Long studentNumber = 20261234L;
        Post post = givenNoticePost(noticeId);

        when(studentJpaRepository.findByNumber(studentNumber)).thenReturn(Optional.empty());

        //when

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> noticeService.deleteNotice(noticeId, studentNumber)
        );

        //then
        assertEquals(CommonErrorCode.INVALID_CREDENTIALS,ex.getErrorCode());
    }

    @Test
    @DisplayName("deleteNotice: ADMIN 아니면 NO_NOTICE_PERMISSION")
    void deleteNotice_fail_notAdmin(){
        //given
        Long noticeId = 1L;
        Long studentNumber = 20261234L;
        Student student = givenStudent(studentNumber);
        Post post = givenNoticePost(noticeId);

        //when
        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> noticeService.deleteNotice(noticeId, studentNumber)
        );

        //then
        assertEquals(NoticeErrorCode.NO_NOTICE_PERMISSION, ex.getErrorCode());
    }

    @Test
    @DisplayName("syncNotice: 성공")
    void syncNotice_success(){
        //given
        NoticeItemRequest item1 = new NoticeItemRequest(
                "공지1",
                "1내용입니다",
                "2026-03-24",
                "https://example1.com",
                1L,
                "제목입니다",
                "ACTIVE"
        );

        NoticeItemRequest item2 = new NoticeItemRequest(
                "공지2",
                "2내용입니다",
                "2026-03-24",
                "https://example2.com",
                2L,
                "제목입니다",
                "ACTIVE"
        );

        NoticeSyncRequest request = new NoticeSyncRequest(List.of(item1,item2));

        Student student = mock(Student.class);
        Board noticeBoard = mock(Board.class);

        when(studentJpaRepository.findById(1L)).thenReturn(Optional.of(student));
        when(boardJpaRepository.findByCategory(BoardCategory.NOTICE)).thenReturn(Optional.of(noticeBoard));
        when(noticeRepository.save(any(Notice.class))).thenAnswer(i -> i.getArguments()[0]);

        //when
        noticeService.sync(request);


        //then
        verify(studentJpaRepository).findById(1L);
        verify(boardJpaRepository).findByCategory(BoardCategory.NOTICE);
        verify(postJpaRepository,times(2)).save(any(Post.class));
    }

    @Test
    @DisplayName("syncNotice: 1L 학생 없을시 NOT_FOUND")
    void syncNotice_fail_studentNotFound() {
        //given
        NoticeItemRequest item = new NoticeItemRequest(
                "공지1",
                "내용입니다",
                "2026-03-24",
                "https://example.com",
                1L,
                "제목입니다",
                "ACTIVE"
        );
        NoticeSyncRequest request = new NoticeSyncRequest(List.of(item));

        when(studentJpaRepository.findById(1L)).thenReturn(Optional.empty());

        //when
        BusinessException ex = assertThrows(BusinessException.class,
                () -> noticeService.sync(request));

        //then
        assertEquals(CommonErrorCode.NOT_FOUND, ex.getErrorCode());
        verify(postJpaRepository,never()).save(any(Post.class));
    }

    @Test
    @DisplayName("syncNotice: NOTICE 게시판이 없으면 NOT_FOUND")
    void syncNotice_fail_noticeBoardNotFound() {
        // given
        NoticeItemRequest item = new NoticeItemRequest(
                "공지1",
                "내용입니다",
                "2026-03-24",
                "https://example.com",
                1L,
                "제목입니다",
                "ACTIVE"
        );
        NoticeSyncRequest request = new NoticeSyncRequest(List.of(item));

        Student student = mock(Student.class);
        when(studentJpaRepository.findById(1L)).thenReturn(Optional.of(student));
        when(boardJpaRepository.findByCategory(BoardCategory.NOTICE))
                .thenReturn(Optional.empty());

        // when
        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> noticeService.sync(request)
        );

        // then
        assertEquals(CommonErrorCode.NOT_FOUND, ex.getErrorCode());
        verify(postJpaRepository, never()).save(any(Post.class));
    }


    private Post givenActiveNoticePost(Long noticeId){
        Post post = mock(Post.class);

        when(postJpaRepository.findById(noticeId)).thenReturn(Optional.of(post));
        when(post.isNotice()).thenReturn(true);
        when(post.getStatus()).thenReturn(PostStatus.ACTIVE);

        return post;
    }

    private Post givenDeletedNoticePost(Long noticeId){
        Post post = mock(Post.class);

        when(postJpaRepository.findById(noticeId)).thenReturn(Optional.of(post));
        when(post.isNotice()).thenReturn(true);
        when(post.getStatus()).thenReturn(PostStatus.DELETED);

        return post;
    }

    private Post givenNoticePost(Long noticeId){
        Post post = mock(Post.class);

        when(postJpaRepository.findById(noticeId)).thenReturn(Optional.of(post));
        when(post.isNotice()).thenReturn(true);
        when(post.getStatus()).thenReturn(PostStatus.ACTIVE);

        return post;
    }

    private Student givenAdmin(Long studentNumber){
        Student admin = mock(Student.class);

        when(studentJpaRepository.findByNumber(studentNumber)).thenReturn(Optional.of(admin));
        when(admin.getRole()).thenReturn(StudentRole.ADMIN);

        return admin;
    }

    private Student givenStudent(Long studentNumber){
        Student student = mock(Student.class);

        when(studentJpaRepository.findByNumber(studentNumber)).thenReturn(Optional.of(student));
        when(student.getRole()).thenReturn(StudentRole.STUDENT);

        return student;
    }



}
