package kitchenpos.exception.badrequest;

public class OrderFailureOnEmptyOrderTableException extends BadRequestException {
    private static final String DEFAULT_MESSAGE = "주문하려는 테이블이 주문 불가 상태입니다";
    private static final String MESSAGE_FORMAT = "주문하려는 테이블이 주문 불가 상태입니다 : %s";

    public OrderFailureOnEmptyOrderTableException() {
        super(DEFAULT_MESSAGE);
    }

    public OrderFailureOnEmptyOrderTableException(final Long invalidOrderTableId) {
        super(String.format(MESSAGE_FORMAT, invalidOrderTableId));
    }
}
