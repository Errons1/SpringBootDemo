package no.oblinor.oblinordemo;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> failedValidation() {
        return new ResponseEntity<>("Invalid datatype", HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseEntity<String> indexOutOfBound(IndexOutOfBoundsException error) {
        log.error(error);
        return new ResponseEntity<>("Index out of bound", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
