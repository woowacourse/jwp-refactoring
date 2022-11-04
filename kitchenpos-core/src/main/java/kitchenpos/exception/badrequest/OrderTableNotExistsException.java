package kitchenpos.exception.badrequest;

public class OrderTableNotExistsException extends BadRequestException {

    public OrderTableNotExistsException() {
        super("주문 테이블이 존재하지 않습니다.");
    }
}
