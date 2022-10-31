package kitchenpos.exception;

public class NotFoundMenuException extends NotFoundException {

    private static final String ERROR_MESSAGE = "존재하지 않는 메뉴입니다.";

    public NotFoundMenuException() {
        super(ERROR_MESSAGE);
    }
}
