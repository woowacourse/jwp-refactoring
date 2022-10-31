package kitchenpos.exception;

public class OrderMenusCountException extends BadRequestException {

    private static final String ERROR_MESSAGE = "주문에 메뉴를 포함해야합니다.";

    public OrderMenusCountException() {
        super(ERROR_MESSAGE);
    }
}
