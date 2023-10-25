package kitchenpos.exception;

import org.springframework.http.HttpStatus;

public abstract class MenuGroupException extends BaseException {

    public MenuGroupException(final String message, final HttpStatus status) {
        super(message, status);
    }

    public static class NotFoundException extends MenuGroupException {

        public NotFoundException(final Long id) {
            super(id + " ID에 해당하는 메뉴 그룹을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
