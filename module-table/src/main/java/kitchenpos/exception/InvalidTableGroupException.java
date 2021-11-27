package kitchenpos.exception;

import org.springframework.http.HttpStatus;

public class InvalidTableGroupException extends TableException {

    public InvalidTableGroupException() {
        super(HttpStatus.BAD_REQUEST, "유효하지 않은 테이블 그룹 입니다.");
    }
}
