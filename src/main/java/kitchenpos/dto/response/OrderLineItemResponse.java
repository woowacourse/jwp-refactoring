package kitchenpos.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderLineItem;

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

    public static OrderLineItemResponse from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                orderLineItem.getMenu().getName(),
                orderLineItem.getMenu().getPrice(),
                orderLineItem.getQuantity()
        );
    }

    public static List<OrderLineItemResponse> from(List<OrderLineItem> orderLineItem) {
        return orderLineItem.stream()
                .map(OrderLineItemResponse::from)
                .collect(Collectors.toList());
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
