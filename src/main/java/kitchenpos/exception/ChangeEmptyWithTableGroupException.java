package kitchenpos.exception;

public class ChangeEmptyWithTableGroupException extends BusinessException {
    public ChangeEmptyWithTableGroupException(Long orderTableId) {
        super(String.format("%d table is still in table group! First, ungroup the table.", orderTableId));
    }
}
