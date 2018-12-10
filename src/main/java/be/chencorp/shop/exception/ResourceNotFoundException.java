package be.chencorp.shop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends MicroServiceException {

    public ResourceNotFoundException(String code, String name, Throwable throwable){
        super(code, name, throwable);
    }

    public ResourceNotFoundException(String code, String name){
        super(code, name);
    }

}
