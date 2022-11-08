package kitchenpos.domain.order;

import java.util.List;
import kitchenpos.domain.order.OrderLineItem;

public interface OrderValidator {

    void validateMenuExists(List<OrderLineItem> orderLineItems, List<Long> menuIds);

    void validateOrderTableEmpty(Long orderTableId);
}
