package kitchenpos.order.application;

public interface OrderValidator {

    void validateTableNotEmpty(final Long orderTableId);
}
