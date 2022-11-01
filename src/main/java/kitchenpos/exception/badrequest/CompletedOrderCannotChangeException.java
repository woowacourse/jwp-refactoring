package kitchenpos.exception.badrequest;

public class CompletedOrderCannotChangeException extends BadRequestException {

    public CompletedOrderCannotChangeException() {
        super("이미 완료된 주문의 상태를 변경할 수 없습니다.");
    }
}
