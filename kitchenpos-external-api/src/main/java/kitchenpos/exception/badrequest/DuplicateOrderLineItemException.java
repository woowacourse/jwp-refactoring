package kitchenpos.exception.badrequest;

public class DuplicateOrderLineItemException extends BadRequestException {

    public DuplicateOrderLineItemException() {
        super("주문 내에 주문 항목이 중복될 수 없습니다.");
    }
}
