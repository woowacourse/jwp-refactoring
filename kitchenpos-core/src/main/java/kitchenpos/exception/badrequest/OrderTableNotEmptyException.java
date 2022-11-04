package kitchenpos.exception.badrequest;

public class OrderTableNotEmptyException extends BadRequestException {

    public OrderTableNotEmptyException() {
        super("빈 테이블이 아닌 테이블을 단체 지정할 수 없습니다.");
    }
}
