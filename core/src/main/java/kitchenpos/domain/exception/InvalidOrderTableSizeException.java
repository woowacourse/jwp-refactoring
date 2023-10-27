package kitchenpos.domain.exception;

public class InvalidOrderTableSizeException extends IllegalArgumentException {

    public InvalidOrderTableSizeException() {
        super("단체 지정을 위해서는 최소 두 개의 주문 테이블이 존재해야 합니다.");
    }
}
