package kitchenpos.exception;

public class ChangeNumberOfGuestsWithAlreadyEmptyTableException extends BusinessException {
    public ChangeNumberOfGuestsWithAlreadyEmptyTableException(Long orderTableId) {
        super(String.format("%d Table can't change number of guests because it is already empty",
            orderTableId));
    }
}
