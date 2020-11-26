package kitchenpos.orderline.application.dto;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.orderline.model.OrderLineItem;

public class OrderLineItemResponseDto {
    private final Long seq;
    private final Long menuId;
    private final long quantity;

    public static List<OrderLineItemResponseDto> listOf(List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
            .map(OrderLineItemResponseDto::from)
            .collect(Collectors.toList());
    }

    public static OrderLineItemResponseDto from(OrderLineItem orderLineItem) {
        return new OrderLineItemResponseDto(orderLineItem.getSeq(), orderLineItem.getMenuId(),
            orderLineItem.getQuantity());
    }

    public OrderLineItemResponseDto(Long seq, Long menuId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
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
}
