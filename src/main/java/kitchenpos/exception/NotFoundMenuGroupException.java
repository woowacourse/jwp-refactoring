package kitchenpos.exception;

public class NotFoundMenuGroupException extends NotFoundException {

    private static final String ERROR_MESSAGE = "존재하지 않는 메뉴 그룹 입니다.";

    public NotFoundMenuGroupException() {
        super(ERROR_MESSAGE);
    }
}
