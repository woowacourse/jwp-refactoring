package kitchenpos.exception;

public class OrderTableConvertEmptyStatusException extends BadRequestException {

    private static final String ERROR_MESSAGE = "빈 상태로 만들 수 없는 상태입니다.";

    public OrderTableConvertEmptyStatusException() {
        super(ERROR_MESSAGE);
    }
}
