package kitchenpos.exception;

import org.springframework.http.HttpStatus;

public class InvalidMenuGroupException extends MenuGroupException {

    public InvalidMenuGroupException() {
        super(HttpStatus.BAD_REQUEST, "유효하지 않은 MenuGroup 입니다.");
    }
}
