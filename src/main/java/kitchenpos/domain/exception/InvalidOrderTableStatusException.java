package kitchenpos.domain.exception;

public class InvalidOrderTableStatusException extends IllegalArgumentException {

    public InvalidOrderTableStatusException() {
        super("지정한 주문 테이블은 계산되지 않아 상태 변경이 불가능합니다.");
    }
}
