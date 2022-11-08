package kitchenpos.table.domain;

public interface OrderTableValidator {

    void validateOnChangeOrderTableEmpty(final OrderTable orderTable);

    void validateExistsNotCompletedOrder(final OrderTable orderTable);
}
