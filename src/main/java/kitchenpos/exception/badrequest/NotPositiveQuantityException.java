package kitchenpos.exception.badrequest;

public class NotPositiveQuantityException extends BadRequestException {

    public NotPositiveQuantityException() {
        super("수량은 0개 이하일 수 없습니다.");
    }
}
