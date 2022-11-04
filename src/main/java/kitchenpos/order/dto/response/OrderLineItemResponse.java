package kitchenpos.order.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private String name;
    private BigDecimal price;
    private long quantity;

    public OrderLineItemResponse(Long seq, Long orderId, Long menuId, String name, BigDecimal price, long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderLineItemResponse(Long orderId, Long menuId, String name, BigDecimal price, long quantity) {
        this(null, orderId, menuId, name, price, quantity);
    }

    public static List<OrderLineItemResponse> from(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItem -> new OrderLineItemResponse(orderLineItem.getSeq(),
                        orderLineItem.getOrderId(),
                        orderLineItem.getMenuId(),
                        orderLineItem.getName(),
                        orderLineItem.getPrice().getValue(),
                        orderLineItem.getQuantity()))
                .collect(Collectors.toList());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
