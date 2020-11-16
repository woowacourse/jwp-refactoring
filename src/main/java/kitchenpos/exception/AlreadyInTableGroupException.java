package kitchenpos.exception;

public class AlreadyInTableGroupException extends BusinessException {
    public AlreadyInTableGroupException(Long orderTableId, Long tableGroupId) {
        super(String.format("%d table is already in table group %d", orderTableId, tableGroupId));
    }
}
