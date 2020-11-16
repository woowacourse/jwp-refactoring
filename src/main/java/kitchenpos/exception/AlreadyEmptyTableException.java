package kitchenpos.exception;

public class AlreadyEmptyTableException extends BusinessException {
    public AlreadyEmptyTableException(Long orderTableId) {
        super(String.format("%d Table can't change number of guests because it is already empty",
            orderTableId));
    }
}
