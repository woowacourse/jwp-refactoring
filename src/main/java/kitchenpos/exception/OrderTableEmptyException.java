package kitchenpos.exception;

public class OrderTableEmptyException extends BadRequestException {

    private static final String ERROR_MESSAGE = "주문 테이블이 비어 있습니다.";

    public OrderTableEmptyException() {
        super(ERROR_MESSAGE);
    }
}
