package kitchenpos.exception;

public enum MenuGroupExceptionType implements BaseExceptionType {

    NOT_EXIST_MENU_GROUP_EXCEPTION("메뉴 그룹이 존재하지 않습니다."),
    ;

    private final String message;

    MenuGroupExceptionType(String message) {
        this.message = message;
    }


    @Override
    public String message() {
        return message;
    }
}
