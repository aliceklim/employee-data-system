package EmployeeDataSystem.exceptions;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@Getter
public class EdsException extends RuntimeException{

    public EdsException(String message, HttpStatus httpStatus){
        super(message);
        log.info(message);
    }

    public EdsException(String message){
        this(message, HttpStatus.BAD_REQUEST);
    }

    public EdsException(Throwable ex, HttpStatus httpStatus){
        this(ex.getMessage(), httpStatus);
    }

}
