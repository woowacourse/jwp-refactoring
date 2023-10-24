package kitchenpos.domain.mapper;

import kitchenpos.domain.OrderLineItem;
import org.springframework.stereotype.Component;

import static kitchenpos.application.dto.request.CreateOrderRequest.CreateOrderLineItem;

@Component
public class OrderLineItemMapper {

    private OrderLineItemMapper() {
    }

    public OrderLineItem toOrderLineItem(CreateOrderLineItem orderLineItem) {
        return OrderLineItem.builder()
                .menuId(orderLineItem.getMenuId())
                .quantity(orderLineItem.getQuantity())
                .build();
    }
}
