package kitchenpos.exception.notfound;

import kitchenpos.exception.badrequest.BadRequestException;

public class OrderTableNotFoundException extends BadRequestException {
    private static final String DEFAULT_MESSAGE = "해당 테이블이 존재하지 않습니다";
    private static final String MESSAGE_FORMAT = "해당 테이블이 존재하지 않습니다 : %s";

    public OrderTableNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public OrderTableNotFoundException(final Long invalidId) {
        super(String.format(MESSAGE_FORMAT, invalidId));
    }
}
