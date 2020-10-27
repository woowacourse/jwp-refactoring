package kitchenpos.domain.exceptions;

public class TableAlreadyHasGroupException extends RuntimeException {
    public TableAlreadyHasGroupException() {
        super("이미 테이블 그룹이 있는 테이블 입니다.");
    }
}
