package kitchenpos.table.domain;

public interface OrderStatusValidator {
    void checkIfOrderIsNotCompleted(Long orderTableId);
}
