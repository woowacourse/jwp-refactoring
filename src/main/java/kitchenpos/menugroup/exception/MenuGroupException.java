package kitchenpos.menugroup.exception;

import kitchenpos.global.exception.CustomException;

public class MenuGroupException extends CustomException {

    public MenuGroupException(final String message) {
        super(message);
    }

    public static class NotFoundMenuGroupException extends MenuGroupException {

        public NotFoundMenuGroupException() {
            super("해당하는 MENU GROUP을 찾을 수 없습니다.");
        }
    }
}
