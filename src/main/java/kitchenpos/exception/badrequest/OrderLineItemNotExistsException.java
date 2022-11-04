package kitchenpos.exception.badrequest;

public class OrderLineItemNotExistsException extends BadRequestException {

    public OrderLineItemNotExistsException() {
        super("주문에 주문 항목이 존재하지 않습니다.");
    }
}
