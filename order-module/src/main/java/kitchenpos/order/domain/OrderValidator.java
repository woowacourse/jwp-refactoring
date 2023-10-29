package kitchenpos.order.domain;

public interface OrderValidator {

    void validatePrepare(final Long orderTableId, final OrderLineItems orderLineItems);
}
