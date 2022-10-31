package kitchenpos.exceptions;

public class OrderAlreadyCompletionException extends RuntimeException {

    public OrderAlreadyCompletionException() {
        super("이미 주문이 완료상태 입니다.");
    }
}
