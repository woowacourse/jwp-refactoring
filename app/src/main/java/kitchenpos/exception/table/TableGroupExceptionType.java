package kitchenpos.exception.table;

import kitchenpos.exception.BaseExceptionType;

public enum TableGroupExceptionType implements BaseExceptionType {

    TABLE_GROUP_NOT_FOUND("테이블 그룹을 찾을 수 없습니다"),
    CAN_NOT_UNGROUP_COOKING_OR_MEAL("조리중이거나 식사중인 테이블의 그룹을 해제할 수 없습니다"),
    ORDER_TABLE_IDS_CAN_NOT_NULL("주문 테이블 아이디들은 널일 수 없습니다"),
    ORDER_TABLES_CAN_NOT_LESS_THAN_TWO("주문 테이블은 두 개 이상이어야 합니다"),
    ORDER_TABLE_MUST_EMPTY("주문 테이블은 비어있어야 합니다"),
    ORDER_TABLE_CAN_NOT_HAVE_TABLE_GROUP("주문 테이블은 테이블 그룹이 없어야 합니다"),
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
