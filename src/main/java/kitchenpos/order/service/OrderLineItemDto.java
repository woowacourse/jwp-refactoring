package kitchenpos.order.service;

import java.util.Objects;
import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemDto {
    private Long seq;
    private Long orderId;
    private Long menuId;
    private long quantity;

    public static OrderLineItemDto from(final OrderLineItem entity, Long orderId) {
        final OrderLineItemDto orderLineItemDto = new OrderLineItemDto();
        orderLineItemDto.setSeq(entity.getSeq());
        orderLineItemDto.setOrderId(orderId);
        orderLineItemDto.setMenuId(entity.getMenu().getId());
        orderLineItemDto.setQuantity(entity.getQuantity());
        return orderLineItemDto;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(final Long orderId) {
        this.orderId = orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        OrderLineItemDto that = (OrderLineItemDto) object;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
