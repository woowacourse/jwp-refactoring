package kitchenpos.exception;

public enum TableGroupExceptionType implements BaseExceptionType {

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
