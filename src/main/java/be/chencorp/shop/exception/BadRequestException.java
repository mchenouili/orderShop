package be.chencorp.shop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends MicroServiceException {

    public BadRequestException(String code, String  message, Throwable throwable){
        super(code,  message, throwable);
    }
    public BadRequestException(String code, String  message){
        super(code,  message);
    }

}
