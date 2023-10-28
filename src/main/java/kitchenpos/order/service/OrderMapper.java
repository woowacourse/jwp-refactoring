package kitchenpos.order.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.service.dto.OrderRequest;

public class OrderMapper {

    private OrderMapper() {
    }

    public static Order toOrder(final OrderRequest request) {
        return new Order(
                request.getOrderTableId(),
                extractOrderLineItems(request)
        );
    }

    private static List<OrderLineItem> extractOrderLineItems(final OrderRequest request) {
        return request.getOrderLineItems().stream()
                .map(each -> new OrderLineItem(
                        each.getMenuId(),
                        each.getQuantity()
                )).collect(Collectors.toList());
    }
}
