package knu.chcse.knucseofficialserver.presentation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import knu.chcse.knucseofficialserver.application.notice.dto.CreateNoticeRequest;
import knu.chcse.knucseofficialserver.application.notice.dto.NoticeResponse;
import knu.chcse.knucseofficialserver.application.notice.dto.UpdateNoticeRequest;
import knu.chcse.knucseofficialserver.global.error.ErrorResponseImpl;
import knu.chcse.knucseofficialserver.global.error.ValidationErrorResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Notice", description = "공지사항 API - 공지사항 생성/조회/수정/삭제 기능을 제공합니다")
public interface NoticeControllerDocs {

    @Operation(
        summary = "공지사항 생성",
        description = "새로운 공지사항을 등록합니다. 관리자 권한이 필요합니다.",
        security = @SecurityRequirement(name = "StudentNumber")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "공지사항 생성 성공. Location 헤더에 생성된 공지사항 URL이 포함됩니다. Body에는 생성된 게시글의 ID가 반환됩니다."
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 - 제목이나 내용이 비어있거나 형식이 올바르지 않습니다.",
            content = @Content(schema = @Schema(implementation = ValidationErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "권한 없음 - 관리자만 공지사항을 작성할 수 있습니다.",
            content = @Content(schema = @Schema(implementation = ErrorResponseImpl.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "사용자를 찾을 수 없음",
            content = @Content(schema = @Schema(implementation = ErrorResponseImpl.class))
        )
    })
    ResponseEntity<Long> createNotice(
        @Parameter(description = "학번 (관리자 권한 필요)", required = true, example = "2021123456")
        Long studentNumber,

        @Parameter(description = "공지사항 생성 요청 데이터", required = true)
        CreateNoticeRequest request
    );

    @Operation(
        summary = "공지사항 단건 조회",
        description = "ID로 특정 공지사항의 상세 정보를 조회합니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "조회 성공"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "공지사항이 아닌 게시글입니다.",
            content = @Content(schema = @Schema(implementation = ErrorResponseImpl.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "공지사항을 찾을 수 없습니다. (존재하지 않거나 삭제됨)",
            content = @Content(schema = @Schema(implementation = ErrorResponseImpl.class))
        )
    })
    ResponseEntity<NoticeResponse> getNotice(
        @Parameter(description = "공지사항 ID", required = true, example = "1")
        Long noticeId
    );

    @Operation(
        summary = "공지사항 목록 조회",
        description = "모든 공지사항을 최신순으로 조회합니다. 삭제된 공지사항은 제외됩니다."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "조회 성공 (결과가 없어도 빈 배열 반환)"
        )
    })
    ResponseEntity<List<NoticeResponse>> getNotices();

    @Operation(
        summary = "공지사항 수정",
        description = "기존 공지사항의 제목과 내용을 수정합니다. 관리자 권한이 필요합니다.",
        security = @SecurityRequirement(name = "StudentNumber")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "수정 성공"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 - 공지사항이 아닌 게시글이거나, 제목/내용이 비어있거나 형식이 올바르지 않습니다.",
            content = @Content(schema = @Schema(oneOf = {
                ErrorResponseImpl.class,
                ValidationErrorResponse.class
            }))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "권한 없음 - 관리자만 수정 가능",
            content = @Content(schema = @Schema(implementation = ErrorResponseImpl.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "공지사항을 찾을 수 없습니다.",
            content = @Content(schema = @Schema(implementation = ErrorResponseImpl.class))
        )
    })
    ResponseEntity<Void> updateNotice(
        @Parameter(description = "공지사항 ID", required = true, example = "1")
        Long noticeId,

        @Parameter(description = "학번 (관리자 권한 필요)", required = true, example = "2021123456")
        Long studentNumber,

        @Parameter(description = "공지사항 수정 요청 데이터", required = true)
        UpdateNoticeRequest request
    );

    @Operation(
        summary = "공지사항 삭제",
        description = "공지사항을 삭제합니다. 실제로는 Soft Delete 방식으로 상태만 변경됩니다. 관리자 권한이 필요합니다.",
        security = @SecurityRequirement(name = "StudentNumber")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "삭제 성공"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "공지사항이 아닌 게시글입니다.",
            content = @Content(schema = @Schema(implementation = ErrorResponseImpl.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "권한 없음 - 관리자만 삭제 가능",
            content = @Content(schema = @Schema(implementation = ErrorResponseImpl.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "공지사항을 찾을 수 없습니다.",
            content = @Content(schema = @Schema(implementation = ErrorResponseImpl.class))
        )
    })
    ResponseEntity<Void> deleteNotice(
        @Parameter(description = "공지사항 ID", required = true, example = "1")
        Long noticeId,

        @Parameter(description = "학번 (관리자 권한 필요)", required = true, example = "2021123456")
        Long studentNumber
    );
}