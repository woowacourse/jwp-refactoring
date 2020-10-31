package kitchenpos.domain.exceptions;

public class InvalidOrderStatusException extends RuntimeException {
    public InvalidOrderStatusException() {
        super("변경할 수 없는 주문 상태입니다.");
    }
}
