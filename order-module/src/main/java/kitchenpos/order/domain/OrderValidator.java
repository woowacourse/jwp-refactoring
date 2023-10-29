package kitchenpos.order.domain;

public interface OrderValidator {

    void validatePrepare(final OrderLineItems orderLineItems);
}
