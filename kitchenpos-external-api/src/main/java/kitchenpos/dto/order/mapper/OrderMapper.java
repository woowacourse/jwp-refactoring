package kitchenpos.dto.order.mapper;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.dto.order.request.OrderCreateRequest;

public interface OrderMapper {

    Order toOrder(OrderCreateRequest orderCreateRequest, List<OrderLineItem> orderLineItems, boolean orderTableEmpty);
}
