package kitchenpos.exception.notfound;

import kitchenpos.exception.badrequest.BadRequestException;

public class MenuGroupNotFoundException extends BadRequestException {
    private static final String DEFAULT_MESSAGE = "메뉴 그룹 아이디가 유효하지 않습니다";
    private static final String MESSAGE_FORMAT = "메뉴 그룹 아이디가 유효하지 않습니다 : %s";

    public MenuGroupNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public MenuGroupNotFoundException(final String invalidId) {
        super(String.format(MESSAGE_FORMAT, invalidId));
    }
}
