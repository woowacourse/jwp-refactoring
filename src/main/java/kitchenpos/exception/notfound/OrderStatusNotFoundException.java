package kitchenpos.exception.notfound;

import kitchenpos.exception.badrequest.BadRequestException;

public class OrderStatusNotFoundException extends BadRequestException {
    private static final String DEFAULT_MESSAGE = "일치하는 주문 상태가 존재하지 않습니다";
    private static final String MESSAGE_FORMAT = "일치하는 주문 상태가 존재하지 않습니다 : %s";

    public OrderStatusNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public OrderStatusNotFoundException(final String invalidStatus) {
        super(String.format(MESSAGE_FORMAT, invalidStatus));
    }
}
