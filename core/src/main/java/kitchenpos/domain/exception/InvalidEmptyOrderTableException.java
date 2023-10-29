package kitchenpos.domain.exception;

public class InvalidEmptyOrderTableException extends IllegalArgumentException {

    public InvalidEmptyOrderTableException() {
        super("지정한 테이블이 비어 있지 않다면 상태를 변경할 수 없습니다.");
    }
}
