package kitchenpos.dto.mapper;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.request.OrderCreateRequest;

public interface OrderMapper {

    Order toOrder(OrderCreateRequest orderCreateRequest, List<OrderLineItem> orderLineItems);
}
