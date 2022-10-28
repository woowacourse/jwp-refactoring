package kitchenpos.dto.mapper;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.request.OrderCreateRequest;
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
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                orderLineItems
        );
    }
}
