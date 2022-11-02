package kitchenpos.table.exception;

import kitchenpos.common.exception.BadRequestException;

public class InvalidTableException extends BadRequestException {

    public InvalidTableException(String message) {
        super(message);
    }
}
