package kopo.poly.controller;

import jakarta.persistence.OptimisticLockException;
import kopo.poly.controller.response.CommonResponse;
import kopo.poly.dto.MsgDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<CommonResponse<MsgDTO>> handleOptimisticLockException(OptimisticLockException e) {

        MsgDTO dto = MsgDTO.builder()
                .result(0)
                .msg("다른 사용자가 먼저 변경했습니다. 다시 시도해주세요. error : " + e.getMessage())
                .build();

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.CONFLICT, HttpStatus.CONFLICT.series().name(), dto));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponse<String>> handleIllegalArgumentException(IllegalArgumentException e) {

        return ResponseEntity.ok(
                CommonResponse.of(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.series().name(), e.getMessage()));
    }

//    서버 내부 오류(Exception) 발생시 예외 처리
//    - 예상하지 못한 오류가 발생할 경우 처리
//    - 클라이언트에게 '500 Internal Server Error' 상태 코드와 함께 메시지를 반환

//    @param e 발생한 Exception 예외 객체
//    @return ResponsEntity<CommonResponse> - HTTP 응답 객체

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<CommonResponse<String>> handleException(Exception e) {
//
//        return ResponseEntity.ok(
//                CommonResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.series().name(), e.getMessage()));
//    }
}
