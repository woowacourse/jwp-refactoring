package kitchenpos.order.dto.response;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private final Long seq;
    private final String name;
    private final BigDecimal price;
    private final Long quantity;

    private OrderLineItemResponse(
            Long seq,
            String name,
            BigDecimal price,
            Long quantity
    ) {
        this.seq = seq;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(OrderLineItem orderLineItem, Menu menu) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                menu.getName(),
                menu.getPrice(),
                orderLineItem.getQuantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getQuantity() {
        return quantity;
    }

}
