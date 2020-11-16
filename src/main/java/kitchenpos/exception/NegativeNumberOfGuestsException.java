package kitchenpos.exception;

public class NegativeNumberOfGuestsException extends BusinessException {
    public NegativeNumberOfGuestsException(Long orderTableId) {
        super(String.format("%d Table cannot change number of guests to negative integer",
            orderTableId));
    }
}
