package kitchenpos.exception;

public class PriceException extends BadRequestException {

    private static final String ERROR_MESSAGE = "유효하지 않은 가격입니다.";

    public PriceException() {
        super(ERROR_MESSAGE);
    }
}
