package knu.chcse.knucseofficialserver.application.notice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NoticeItemRequest (
    String category,

    String content,

    @JsonProperty("createdAt")
    String createdAt,

    String link,

    Long num,

    String title,

    String status
){

}