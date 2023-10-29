package kitchenpos.order.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.vo.Menus;

public class OrderCreateRequest {

    private Long orderTableId;
    private List<OrderLineItemCreateRequest> orderLineItems;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId, List<OrderLineItemCreateRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Order toOrder(Menus menus) {
        List<OrderLineItem> orderLineItems = this.orderLineItems.stream()
                .map(request -> request.toOrderLineItem(menus.getMenuSpecificationById(request.getMenuId())))
                .collect(Collectors.toList());
        return Order.of(orderTableId, orderLineItems, menus.counts());
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(Collectors.toList());
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
