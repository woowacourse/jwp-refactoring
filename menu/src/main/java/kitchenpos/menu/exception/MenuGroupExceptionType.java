package kitchenpos.menu.exception;

import kitchenpos.exception.BaseExceptionType;

public enum MenuGroupExceptionType implements BaseExceptionType {

    MENU_GROUP_NOT_FOUND("메뉴 그룹을 찾을 수 없습니다"),
    ;

    private final String errorMessage;

    MenuGroupExceptionType(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }
}
