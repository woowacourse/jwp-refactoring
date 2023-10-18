package kitchenpos.exception;

public enum TableGroupExceptionType implements BaseExceptionType {

    TABLE_GROUP_NOT_EXISTS("테이블 그룹이 존재하지 않습니다."),
    ILLEGAL_ADD_ORDER_TABLE_EXCEPTION("테이블을 테이블 그룹으로 저장하는 과정에서 예외가 발생했습니다."),
    ;

    private final String message;

    TableGroupExceptionType(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return null;
    }
}
