package be.chencorp.shop.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class MicroServiceException extends RuntimeException {

    private String code;

    public MicroServiceException(String code, String message, Throwable throwable){
        super(message, throwable);
        this.code = code;
    }

    public MicroServiceException(String code, String message){
        super(message);
        this.code = code;
    }

}
