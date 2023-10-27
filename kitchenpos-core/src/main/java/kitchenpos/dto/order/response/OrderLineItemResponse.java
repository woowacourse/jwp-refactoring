package kitchenpos.dto.order.response;

import java.util.List;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderedMenu;
import kitchenpos.domain.order.OrderedMenuProduct;

public class OrderLineItemResponse {
    private final Long seq;
    private final OrderedMenuResponse orderedMenuResponse;
    private final long quantity;

    public OrderLineItemResponse(final Long seq, final OrderedMenuResponse orderedMenuResponse, final long quantity) {
        this.seq = seq;
        this.orderedMenuResponse = orderedMenuResponse;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(
            final OrderLineItem orderLineItem,
            final OrderedMenu orderedMenu,
            final List<OrderedMenuProduct> orderedMenuProducts
    ) {
        return new OrderLineItemResponse(
                orderLineItem.seq(),
                OrderedMenuResponse.of(orderedMenu, orderedMenuProducts),
                orderLineItem.quantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public OrderedMenuResponse getOrderedMenuResponse() {
        return orderedMenuResponse;
    }

    public long getQuantity() {
        return quantity;
    }
}
