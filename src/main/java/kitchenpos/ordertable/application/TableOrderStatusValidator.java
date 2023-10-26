package kitchenpos.ordertable.application;

public interface TableOrderStatusValidator {

    void validateOrderIsCompleted(final Long orderTableId);
}
