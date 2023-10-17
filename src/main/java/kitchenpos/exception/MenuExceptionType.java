package kitchenpos.exception;

public enum MenuExceptionType implements BaseExceptionType {

    MENU_NOT_FOUND("메뉴를 찾을 수 없습니다"),
    ;

    private final String errorMessage;

    MenuExceptionType(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }
}
