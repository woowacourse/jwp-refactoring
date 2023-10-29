package kitchenpos.order.application.response;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderLineItemResponse {

    private long seq;
    private Long menuId;
    private BigDecimal menuPrice;
    private long quantity;

    public OrderLineItemResponse(final long seq, final Long menuId, final BigDecimal menuPrice, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.menuPrice = menuPrice;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem, final Menu menu) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                orderLineItem.getMenuId(),
                menu.getPrice().getValue(),
                orderLineItem.getQuantity().getValue()
        );
    }

    public static List<OrderLineItemResponse> from(final OrderLineItems orderLineItems, final List<Menu> menus) {
        final List<OrderLineItemResponse> responses = new ArrayList<>();
        for (int index = 0; index < orderLineItems.getOrderLineItems().size(); index++) {
            responses.add(from(orderLineItems.getOrderLineItems().get(index), menus.get(index)));
        }

        return responses;
    }

    public long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }

    public long getQuantity() {
        return quantity;
    }
}
