//package com.towerdefense.leveleditor.http.exception.handler;
//
//import java.io.IOException;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.ErrorResponse;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
//        return ResponseEntity.badRequest()
//            .body(new ErrorResponse(ex.getMessage()));
//    }
//
//    @ExceptionHandler(IOException.class)
//    public ResponseEntity<ErrorResponse> handleIOException(IOException ex) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//            .body(new ErrorResponse("File operation failed: " + ex.getMessage()));
//    }
//}
