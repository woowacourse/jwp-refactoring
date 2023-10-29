package kitchenpos.domain.exception;

public class InvalidOrderTableException extends IllegalArgumentException {

    public InvalidOrderTableException() {
        super("지정한 주문 테이블이 존재하지 않아 단체 지정이 불가능합니다.");
    }
}
