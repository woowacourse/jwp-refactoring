package kitchenpos.domain;

public interface OrderTableValidator {

    void validateChangeEmpty(OrderTable orderTable);

    void validateUngroup(OrderTable orderTable, String errorMessage);
}
