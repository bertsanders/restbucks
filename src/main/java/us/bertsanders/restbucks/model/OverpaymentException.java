package us.bertsanders.restbucks.model;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OverpaymentException extends RuntimeException {

    public OverpaymentException() {
        super();
    }

    public OverpaymentException(String message) {
        super(message);
    }
}
