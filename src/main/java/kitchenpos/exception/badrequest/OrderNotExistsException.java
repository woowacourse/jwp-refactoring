package kitchenpos.exception.badrequest;

public class OrderNotExistsException extends BadRequestException {

    public OrderNotExistsException() {
        super("존재하지 않는 주문입니다.");
    }
}
