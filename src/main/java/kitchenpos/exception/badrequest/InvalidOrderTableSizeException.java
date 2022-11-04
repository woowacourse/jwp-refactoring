package kitchenpos.exception.badrequest;

public class InvalidOrderTableSizeException extends BadRequestException {

    public InvalidOrderTableSizeException() {
        super("단체 지정하려는 테이블은 2개 미만일 수 없습니다.");
    }
}
