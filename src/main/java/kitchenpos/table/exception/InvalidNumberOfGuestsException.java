package kitchenpos.table.exception;

import kitchenpos.common.exception.InvalidRequestException;

public class InvalidNumberOfGuestsException extends InvalidRequestException {

    public InvalidNumberOfGuestsException() {
        super("올바르지 않은 손님 수입니다.");
    }
}
