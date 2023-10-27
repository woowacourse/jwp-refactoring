package kitchenpos.dto;

import java.util.List;

public class OrderCreateRequest {

    private Long orderTableId;
    private final List<OrderLineItemDto> orderLineItems;

    public OrderCreateRequest(Long orderTableId, List<OrderLineItemDto> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemDto> getOrderLineItems() {
        return orderLineItems;
    }

    public static class OrderLineItemDto {

        private Long menuId;
        private Long quantity;

        public OrderLineItemDto(Long menuId, Long quantity) {
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public Long getMenuId() {
            return menuId;
        }

        public Long getQuantity() {
            return quantity;
        }
    }
}
