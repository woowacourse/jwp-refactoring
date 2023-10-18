package kitchenpos.exception;

public class TableGroupNotFoundException extends RuntimeException{
    private final static String error = "TableGroup을 찾을 수 없습니다.";
    public TableGroupNotFoundException() {
        super(error);
    }
}
