package ru.weber.login.service.handler;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.weber.login.service.dto.ErrorResponse;
import ru.weber.login.service.exception.LoginExistException;
import ru.weber.login.service.exception.UserNotFoundException;


@Log4j2
@ControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        log.info(ex.getMessage());
        return new ResponseEntity(
                new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleLoginExistEcxeption(LoginExistException ex) {
        log.info(ex.getMessage());
        return new ResponseEntity(
                new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), System.currentTimeMillis()),
                HttpStatus.BAD_REQUEST);
    }
}
