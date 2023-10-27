package kitchenpos.domain.orertable;

public interface OrderTableValidator {

    void validateChangeEmpty(final Long orderTableId, final OrderTable orderTable);
}
