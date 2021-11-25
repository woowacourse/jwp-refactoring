package kitchenpos.table.exception;

import kitchenpos.exception.KitchenposException;
import org.springframework.http.HttpStatus;

public class InvalidTableGroupException extends KitchenposException {

    public InvalidTableGroupException() {
        super(HttpStatus.BAD_REQUEST, "유효하지 않은 테이블 그룹 입니다.");
    }
}
