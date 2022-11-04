package kitchenpos.order.application;

import kitchenpos.order.dto.OrderCreateRequest;

public interface OrderTableValidator {
    Long validOrderTableAndGet(final OrderCreateRequest orderCreateRequest);
}
