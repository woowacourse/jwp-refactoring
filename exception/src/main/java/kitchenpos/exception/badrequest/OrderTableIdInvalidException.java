package kitchenpos.exception.badrequest;

public class OrderTableIdInvalidException extends BadRequestException {
    private static final String DEFAULT_MESSAGE = "테이블 아이디가 유효하지 않습니다";
    private static final String MESSAGE_FORMAT = "테이블 아이디가 유효하지 않습니다 : %d";

    public OrderTableIdInvalidException() {
        super(DEFAULT_MESSAGE);
    }

    public OrderTableIdInvalidException(final Long invalidOrderTableId) {
        super(String.format(MESSAGE_FORMAT, invalidOrderTableId));
    }
}


