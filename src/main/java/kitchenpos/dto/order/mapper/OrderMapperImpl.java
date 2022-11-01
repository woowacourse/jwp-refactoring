package kitchenpos.dto.order.mapper;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.dto.order.request.OrderCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public Order toOrder(final OrderCreateRequest orderCreateRequest, final List<OrderLineItem> orderLineItems) {
        return new Order(orderCreateRequest.getOrderTableId(), orderLineItems);
    }
}
