package kitchenpos.order.ui.dto.response;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private Long order;
    private Long menu;
    private Long quantity;

    private OrderLineItemResponse() {
    }

    private OrderLineItemResponse(Long seq, Long order, Long menu, Long quantity) {
        this.seq = seq;
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse create(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                orderLineItem.getOrderId(),
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity()
        );
    }

    public static List<OrderLineItemResponse> of(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItemResponse::create)
                .collect(toList());
    }


    public Long getSeq() {
        return seq;
    }

    public Long getOrder() {
        return order;
    }

    public Long getMenu() {
        return menu;
    }

    public Long getQuantity() {
        return quantity;
    }
}
