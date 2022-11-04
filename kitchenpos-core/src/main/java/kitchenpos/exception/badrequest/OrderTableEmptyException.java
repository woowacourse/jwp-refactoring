package kitchenpos.exception.badrequest;

public class OrderTableEmptyException extends BadRequestException {

    public OrderTableEmptyException() {
        super("빈 테이블에는 주문을 할 수 없습니다.");
    }
}
