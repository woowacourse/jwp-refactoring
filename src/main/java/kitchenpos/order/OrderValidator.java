package kitchenpos.order;

import java.util.List;

public interface OrderValidator {

    void validate(Long orderTableId, List<OrderLineItem> orderLineItems);
}
