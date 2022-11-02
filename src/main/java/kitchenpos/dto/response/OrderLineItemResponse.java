package kitchenpos.dto.response;

import java.math.BigDecimal;
import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private Long menuId;
    private long quantity;
    private String menuName;
    private BigDecimal menuPrice;

    private OrderLineItemResponse() {
    }

    private OrderLineItemResponse(final Long seq, final Long menuId, final long quantity, final String menuName,
                                  final BigDecimal menuPrice) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }

    public static OrderLineItemResponse from(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getSeq(), orderLineItem.getMenuId(),
                orderLineItem.getQuantity(), orderLineItem.getMenuName(), orderLineItem.getPrice());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public String getMenuName() {
        return menuName;
    }

    public BigDecimal getMenuPrice() {
        return menuPrice;
    }
}
