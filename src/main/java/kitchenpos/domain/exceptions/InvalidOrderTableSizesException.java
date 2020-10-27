package kitchenpos.domain.exceptions;

public class InvalidOrderTableSizesException extends RuntimeException {
    public InvalidOrderTableSizesException() {
        super("테이블 그룹은 최소 2개 주문 테이블이 필요합니다.");
    }
}
