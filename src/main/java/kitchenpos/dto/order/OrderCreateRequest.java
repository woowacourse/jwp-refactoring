package kitchenpos.dto.order;

import kitchenpos.domain.order.OrderLineItemDtos;

import java.util.List;

public class OrderCreateRequest {
    private Long orderTableId;
    private List<OrderLineItemDto> orderLineItemDtos;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(Long orderTableId, List<OrderLineItemDto> orderLineItemDtos) {
        this.orderTableId = orderTableId;
        this.orderLineItemDtos = orderLineItemDtos;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemDto> getOrderLineItemDtos() {
        return orderLineItemDtos;
    }

    public OrderLineItemDtos toOrderLineItemDtos() {
        return OrderLineItemDtos.from(this.orderLineItemDtos);
    }

    public static class OrderLineItemDto {
        private Long menuId;
        private long quantity;

        public OrderLineItemDto() {
        }

        public OrderLineItemDto(Long menuId, long quantity) {
            this.menuId = menuId;
            this.quantity = quantity;
        }

        public Long getMenuId() {
            return menuId;
        }

        public long getQuantity() {
            return quantity;
        }
    }
}
