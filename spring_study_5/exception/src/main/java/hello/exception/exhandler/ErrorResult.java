package hello.exception.exhandler;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * API 예외처리 응답 스펙
 */
@Data
@AllArgsConstructor
public class ErrorResult {
    private String code;
    private String message;
}
