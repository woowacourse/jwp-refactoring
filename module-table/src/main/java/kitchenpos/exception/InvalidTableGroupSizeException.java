package kitchenpos.exception;

import org.springframework.http.HttpStatus;

public class InvalidTableGroupSizeException extends TableException {

    public InvalidTableGroupSizeException() {
        super(HttpStatus.BAD_REQUEST, "유효하지 않은 테이블 그룹 사이즈 입니다.");
    }
}
