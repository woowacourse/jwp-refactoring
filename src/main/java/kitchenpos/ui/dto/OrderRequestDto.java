package kitchenpos.ui.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateOrderDto;
import kitchenpos.application.dto.CreateOrderLineItemDto;

public class OrderRequestDto {

    private Long orderTableId;
    private List<OrderLineItemRequestDto> orderLineItems;

    public OrderRequestDto() {
    }

    public CreateOrderDto toCreateOrderDto() {
        return new CreateOrderDto(orderTableId, orderLineItems.stream()
                .map(OrderLineItemRequestDto::toCreateOrderLineItemDto)
                .collect(Collectors.toList()));
    }

    public void setOrderTableId(Long orderTableId) {
        this.orderTableId = orderTableId;
    }

    public void setOrderLineItems(List<OrderLineItemRequestDto> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    static class OrderLineItemRequestDto {

        private Long menuId;
        private Integer quantity;

        public OrderLineItemRequestDto() {
        }

        public CreateOrderLineItemDto toCreateOrderLineItemDto() {
            return new CreateOrderLineItemDto(menuId, quantity);
        }

        public void setMenuId(Long menuId) {
            this.menuId = menuId;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}
