package kitchenpos.exception.orderException;

public class IllegalOrderStatusException extends OrderExcpetion {
    private final static String error = "잘못된 주문 상태입니다.";
    public IllegalOrderStatusException() {
        super(error);
    }
}
