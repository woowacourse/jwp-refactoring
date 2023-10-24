package kitchenpos.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long id;
    private MenuResponse menu;
    private Long quantity;

    public OrderLineItemResponse(Long id, MenuResponse menu, Long quantity) {
        this.id = id;
        this.menu = menu;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse toResponse(OrderLineItem orderLineItem){
        return new OrderLineItemResponse(orderLineItem.getId(),
                MenuResponse.toResponse(orderLineItem.getMenu()),
                orderLineItem.getQuantity());
    }

    public Long getId() {
        return id;
    }

    public MenuResponse getMenu() {
        return menu;
    }

    public Long getQuantity() {
        return quantity;
    }
}
