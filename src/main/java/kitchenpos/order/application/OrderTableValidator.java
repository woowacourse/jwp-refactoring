package kitchenpos.order.application;

import kitchenpos.order.dto.OrderCreateRequest;

public interface OrderTableValidator {
    void validOrderTable(final OrderCreateRequest orderCreateRequest);
}
