package kitchenpos.domain.exception;

public class InvalidOrderStatusException extends IllegalArgumentException {

    public InvalidOrderStatusException() {
        super("계산 완료된 주문의 상태를 변경할 수 없습니다.");
    }
}
