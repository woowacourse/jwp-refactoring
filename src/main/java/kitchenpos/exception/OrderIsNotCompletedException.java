package kitchenpos.exception;

import org.springframework.remoting.RemoteTimeoutException;

public class OrderIsNotCompletedException extends RemoteTimeoutException {

    private static final String MESSAGE = "주문이 아직 완료 단계가 아닙니다.";

    public OrderIsNotCompletedException() {
        super(MESSAGE);
    }
}
