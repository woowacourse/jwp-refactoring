package kitchenpos.exception;

public enum TableGroupExceptionType implements BaseExceptionType {

    TABLE_GROUP_NOT_FOUND("테이블 그룹을 찾을 수 없습니다"),
    CAN_NOT_UNGROUP_COOKING_OR_MEAL("조리중이거나 식사중인 테이블의 그룹을 해제할 수 없습니다"),
    ;

    private final String errorMessage;

    TableGroupExceptionType(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }
}
