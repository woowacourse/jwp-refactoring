package kitchenpos.exception;

public enum OrderTableExceptionType implements BaseExceptionType {

    NOT_EXIST_ORDER_TABLE("주문 테이블이 존재하지 않습니다."),
    TABLE_GROUP_IS_NOT_NULL_EXCEPTION("테이블 그룹이 null이 아닐 땐 테이블의 empty 상태를 변경할 수 없습니다."),
    NUMBER_OF_GUESTS_IS_NULL_OR_NEGATIVE_EXCEPTION("손님의 수는 널이거나 음수일 수 없습니다."),
    ILLEGAL_CHANGE_NUMBER_OF_GUESTS("테이블이 Empty 상태이면 손님의 수를 변경할 수 없습니다."),
    ORDER_TABLE_SIZE_NOT_ENOUGH("테이블 수가 부족합니다."),
    ORDER_STATUS_IS_NOT_COMPLETION("아직 계산 완료가 된 상태가 아닙니다. 테이블 그룹을 해제할 수 없습니다."),
    ;

    private final String message;

    OrderTableExceptionType(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return message;
    }
}
