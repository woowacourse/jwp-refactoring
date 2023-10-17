package kitchenpos.exception;

public class MenuException extends CustomException {

    public MenuException(final String message) {
        super(message);
    }

    public static class NotFoundMenuException extends MenuException {

        public NotFoundMenuException() {
            super("해당하는 MENU를 찾을 수 없습니다.");
        }
    }
}
