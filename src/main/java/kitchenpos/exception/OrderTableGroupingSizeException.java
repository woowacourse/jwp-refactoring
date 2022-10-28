package kitchenpos.exception;

public class OrderTableGroupingSizeException extends BadRequestException {

    private static final String ERROR_MESSAGE = "2개 미만의 주문 테이블은 그룹화 할 수 없습니다.";

    public OrderTableGroupingSizeException() {
        super(ERROR_MESSAGE);
    }
}
