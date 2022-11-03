package kitchenpos.fixture;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderRequest.OrderInnerLineItems;

public class OrderRequestFactory {

    public static OrderRequest orderRequestFrom(final Order order) {
        return new OrderRequest(
                order.getOrderTableId(),
                mapToOrderInnerLineItems(order.getOrderLineItems()));
    }

    private static List<OrderInnerLineItems> mapToOrderInnerLineItems(final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItem -> new OrderInnerLineItems(
                        orderLineItem.getMenuId(),
                        orderLineItem.getQuantity()))
                .collect(Collectors.toList());
    }
}
