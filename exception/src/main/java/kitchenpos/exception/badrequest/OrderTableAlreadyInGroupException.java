package kitchenpos.exception.badrequest;

public class OrderTableAlreadyInGroupException extends BadRequestException {
    private static final String DEFAULT_MESSAGE = "이미 단체에 소속된 테이블입니다";
    private static final String MESSAGE_FORMAT = "이미 단체에 소속된 테이블입니다 : %s";

    public OrderTableAlreadyInGroupException() {
        super(DEFAULT_MESSAGE);
    }

    public OrderTableAlreadyInGroupException(final Long groupId) {
        super(String.format(MESSAGE_FORMAT, groupId));
    }
}
