package kitchenpos.menu.domain.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemDto {

    private Long menuId;
    private long quantity;
    private String menuName;
    private String price;

    protected OrderLineItemDto() {
    }

    public OrderLineItemDto(Long menuId, long quantity, String menuName, String price) {
        this.menuId = menuId;
        this.quantity = quantity;
        this.menuName = menuName;
        this.price = price;
    }

    public static OrderLineItemDto from(OrderLineItem orderLineItem) {
        return new OrderLineItemDto(
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity().getValue(),
                orderLineItem.getMenuName(),
                orderLineItem.getMenuPrice().toString()
        );
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

    public String getPrice() {
        return price;
    }
}
