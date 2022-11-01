package kitchenpos.exception.badrequest;

public class MenuGroupNameInvalidException extends BadRequestException {
    private static final String DEFAULT_MESSAGE = "메뉴그룹명이 유효하지 않습니다";
    private static final String MESSAGE_FORMAT = "메뉴그룹명이 유효하지 않습니다 : %s";

    public MenuGroupNameInvalidException() {
        super(DEFAULT_MESSAGE);
    }

    public MenuGroupNameInvalidException(final String invalidName) {
        super(String.format(MESSAGE_FORMAT, invalidName));
    }
}
