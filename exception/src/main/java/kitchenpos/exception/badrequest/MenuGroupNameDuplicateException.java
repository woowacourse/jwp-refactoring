package kitchenpos.exception.badrequest;

public class MenuGroupNameDuplicateException extends BadRequestException {
    private static final String DEFAULT_MESSAGE = "메뉴그룹명은 중복될 수 없습니다";
    private static final String MESSAGE_FORMAT = "메뉴그룹명은 중복될 수 없습니다 : %s";

    public MenuGroupNameDuplicateException() {
        super(DEFAULT_MESSAGE);
    }

    public MenuGroupNameDuplicateException(final String duplicateName) {
        super(String.format(MESSAGE_FORMAT, duplicateName));
    }
}
