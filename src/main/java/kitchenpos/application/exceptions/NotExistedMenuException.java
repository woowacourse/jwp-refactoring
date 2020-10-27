package kitchenpos.application.exceptions;

public class NotExistedMenuException extends RuntimeException {
    public NotExistedMenuException() {
        super("존재하지 않는 메뉴입니다.");
    }
}
