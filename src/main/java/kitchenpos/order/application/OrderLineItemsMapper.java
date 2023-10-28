package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.order.application.dto.OrderCreateRequest.OrderLineItemCreateRequest;
import kitchenpos.order.domain.OrderLineItem;
import org.springframework.stereotype.Component;

@Component
public class OrderLineItemsMapper {

    public List<OrderLineItem> mapFrom(final List<OrderLineItemCreateRequest> requestOrderLineItems) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (final OrderLineItemCreateRequest requestOrderLineItem : requestOrderLineItems) {
            final OrderLineItem orderLineItem = new OrderLineItem(
                    requestOrderLineItem.getMenuId(),
                    requestOrderLineItem.getQuantity()
            );
            orderLineItems.add(orderLineItem);
        }
        return orderLineItems;
    }
}
