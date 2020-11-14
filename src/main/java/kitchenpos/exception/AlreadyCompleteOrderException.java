package kitchenpos.exception;

public class AlreadyCompleteOrderException extends BusinessException {

    public AlreadyCompleteOrderException(Long orderId) {
        super(String.format("%d order is already complete status.", orderId));
    }
}
