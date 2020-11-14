package kitchenpos.exception;

public class OrderStatusNotFoundException extends RuntimeException {
    public OrderStatusNotFoundException(String orderStatus) {
        super(orderStatus + "라는 주문 상태는 존재하지 않습니다!");
    }
}
