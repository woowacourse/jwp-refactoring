package kitchenpos.order.exception;

public class OrderNotFoundException extends OrderExcpetion {
    public static final String error = "주문을 찾을 수 없습니다.";
    public OrderNotFoundException() {
        super(error);
    }
}
