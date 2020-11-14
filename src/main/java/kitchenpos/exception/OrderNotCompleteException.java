package kitchenpos.exception;

public class OrderNotCompleteException extends BusinessException {
    public OrderNotCompleteException(Long orderTableId) {
        super(String.format("Order of %d table is not completion yet. First, complete order.",
            orderTableId));
    }
}
