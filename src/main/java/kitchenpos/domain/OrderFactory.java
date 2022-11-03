package kitchenpos.domain;

import java.util.List;
import java.util.stream.Collectors;

public class OrderFactory {

    private OrderFactory() {
    }

    public static Order create(final Order orderEntity, final List<OrderLineItem> orderLineItemEntities) {
        return new Order(
                orderEntity.getId(),
                orderEntity.getOrderTableId(),
                orderEntity.getOrderStatus(),
                orderEntity.getOrderedTime(),
                createOrderLineItems(orderEntity.getId(), orderLineItemEntities)
        );
    }

    private static List<OrderLineItem> createOrderLineItems(final Long orderId,
                                                            final List<OrderLineItem> orderLineItemEntities) {
        return orderLineItemEntities.stream()
                .map(it -> new OrderLineItem(
                        it.getSeq(),
                        orderId,
                        it.getMenuId(),
                        it.getMenuName(),
                        it.getMenuPrice(),
                        it.getQuantity()))
                .collect(Collectors.toList());
    }
}
