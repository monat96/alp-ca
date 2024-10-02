package com.kt.nms.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private HttpStatus status;
    private String message;

    @Builder.Default
    private long timestamp = Instant.now().toEpochMilli();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;


}
