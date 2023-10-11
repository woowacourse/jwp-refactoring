package kitchenpos.exception;

public class MenuGroupException extends RuntimeException {

    public MenuGroupException(final String message) {
        super(message);
    }

    public static class NotFoundMenuGroupException extends MenuGroupException {

        public NotFoundMenuGroupException() {
            super("[ERROR] 해당하는 MENU GROUP을 찾을 수 없습니다.");
        }
    }
}
