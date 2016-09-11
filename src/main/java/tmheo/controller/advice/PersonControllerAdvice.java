package tmheo.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import tmheo.exception.ResourceNotFoundException;
import tmheo.model.ErrorResponse;

/**
 * Created by taemyung on 2016. 9. 11..
 */
@ControllerAdvice
@Slf4j
public class PersonControllerAdvice {

    @ResponseBody
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<?> resourceFoundExceptionHandler(ResourceNotFoundException ex) {

        log.error("resource not found error", ex);

        return new ResponseEntity<>(new ErrorResponse("resource not found error", ex.getMessage()), HttpStatus.NOT_FOUND);

    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<?> badRequestExceptionHandler(MethodArgumentNotValidException ex) {

        log.error("bad request error", ex);

        return new ResponseEntity<>(new ErrorResponse("bad request error", ex.getMessage()), HttpStatus.BAD_REQUEST);

    }

}
