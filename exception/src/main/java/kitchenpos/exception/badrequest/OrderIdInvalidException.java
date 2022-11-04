package kitchenpos.exception.badrequest;

public class OrderIdInvalidException extends BadRequestException {
    private static final String DEFAULT_MESSAGE = "주문 아이디가 유효하지 않습니다";
    private static final String MESSAGE_FORMAT = "주문 아이디가 유효하지 않습니다 : %d";

    public OrderIdInvalidException() {
        super(DEFAULT_MESSAGE);
    }

    public OrderIdInvalidException(final Long invalidOrderId) {
        super(String.format(MESSAGE_FORMAT, invalidOrderId));
    }
}


