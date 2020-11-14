package kitchenpos.exception;

public class TableGroupWithNotEmptyTableException extends BusinessException {
    public TableGroupWithNotEmptyTableException(Long orderTableId) {
        super(String.format("%d table is not empty Table. For group tables, first change empty",
            orderTableId));
    }
}
