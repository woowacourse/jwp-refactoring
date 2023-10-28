package kitchenpos.table.domain;

public interface OrderTableValidator {

    void validateChangeStatusEmpty(OrderTable orderTable);

    void validateChangingNumberOfGuests(OrderTable orderTable);
}
