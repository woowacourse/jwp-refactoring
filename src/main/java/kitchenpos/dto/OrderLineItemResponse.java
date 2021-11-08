package kitchenpos.dto;

import kitchenpos.domain.OrderLineItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItemResponse {

    private Long id;
    private Long seq;
    private Long menuId;
    private long quantity;

    public OrderLineItemResponse(Long id, Long seq, Long menuId, long quantity) {
        this.id = id;
        this.seq = seq;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(OrderLineItem orderLineItem){
        return new OrderLineItemResponse(orderLineItem.getId(), orderLineItem.getSeq(), orderLineItem.getMenuId(), orderLineItem.getQuantity());
    }

    public static List<OrderLineItemResponse> listOf(List<OrderLineItem> orderLineItems){
        return orderLineItems.stream()
                .map(OrderLineItemResponse::of)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
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
