package kitchenpos.ui.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.CreateOrderDto;
import kitchenpos.application.dto.request.CreateOrderLineItemDto;

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

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequestDto> getOrderLineItems() {
        return orderLineItems;
    }

    static class OrderLineItemRequestDto {

        private Long menuId;
        private Integer quantity;

        public OrderLineItemRequestDto() {
        }

        public CreateOrderLineItemDto toCreateOrderLineItemDto() {
            return new CreateOrderLineItemDto(menuId, quantity);
        }

        public Long getMenuId() {
            return menuId;
        }

        public Integer getQuantity() {
            return quantity;
        }
    }
}
