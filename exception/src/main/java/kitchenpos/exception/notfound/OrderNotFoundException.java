package kitchenpos.exception.notfound;

import kitchenpos.exception.badrequest.BadRequestException;

public class OrderNotFoundException extends BadRequestException {
    private static final String DEFAULT_MESSAGE = "해당 주문이 존재하지 않습니다";
    private static final String MESSAGE_FORMAT = "해당 주문이 존재하지 않습니다 : %s";

    public OrderNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public OrderNotFoundException(final Long invalidId) {
        super(String.format(MESSAGE_FORMAT, invalidId));
    }
}
