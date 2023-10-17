package kitchenpos.exception;

public enum OrderTableExceptionType implements BaseExceptionType {

    ORDER_TABLE_NOT_FOUND("주문 테이블을 찾을 수 없습니다"),
    ;

    private final String errorMessage;

    OrderTableExceptionType(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }
}
