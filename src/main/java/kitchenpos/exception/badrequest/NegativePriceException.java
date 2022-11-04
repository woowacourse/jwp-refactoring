package kitchenpos.exception.badrequest;

public class NegativePriceException extends BadRequestException {

    public NegativePriceException() {
        super("가격은 0원 미만일 수 없습니다.");
    }
}
