package kitchenpos.dto.order.mapper;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.common.OrderStatus;
import kitchenpos.dto.order.request.OrderCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public Order toOrder(final OrderCreateRequest orderCreateRequest, final List<OrderLineItem> orderLineItems) {
        if (orderCreateRequest == null) {
            return null;
        }

        return new Order(
                null,
                orderCreateRequest.getOrderTableId(),
                OrderStatus.COOKING,
                LocalDateTime.now(),
                orderLineItems
        );
    }
}
