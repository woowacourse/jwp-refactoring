package kitchenpos.ordertable.domain;

public interface OrderTableValidator {

    void validateChangeEmpty(final Long orderTableId, final OrderTable orderTable);
}
