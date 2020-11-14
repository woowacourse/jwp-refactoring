package kitchenpos.exception;

public class OrderNotFoundException extends BusinessException {
    public OrderNotFoundException(Long orderId) {
        super(String.format("%d order is not exist", orderId));
    }
}
