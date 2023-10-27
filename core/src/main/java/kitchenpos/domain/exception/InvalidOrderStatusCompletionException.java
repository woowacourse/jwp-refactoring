package kitchenpos.domain.exception;

public class InvalidOrderStatusCompletionException extends IllegalArgumentException {

    public InvalidOrderStatusCompletionException() {
        super("지정한 주문은 계산이 완료되어 변경할 수 없습니다.");
    }
}
