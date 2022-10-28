package kitchenpos.exception;

public class NotFoundProductException extends NotFoundException {

    private static final String ERROR_MESSAGE = "존재하지 않는 상품입니다.";

    public NotFoundProductException() {
        super(ERROR_MESSAGE);
    }
}
