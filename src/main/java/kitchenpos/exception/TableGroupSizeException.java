package kitchenpos.exception;

public class TableGroupSizeException extends BusinessException {
    public TableGroupSizeException(int size) {
        super(String.format("%d size table group request is invalid", size));
    }
}
