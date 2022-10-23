package kitchenpos.ui.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateOrderDto;
import kitchenpos.application.dto.CreateOrderLineItemDto;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public class OrderRequestDto {

    private Long orderTableId;
    private List<OrderLineItemRequestDto> orderLineItems;

    public CreateOrderDto toCreateOrderDto() {
        return new CreateOrderDto(orderTableId, orderLineItems.stream()
                .map(OrderLineItemRequestDto::toCreateOrderLineItemDto)
                .collect(Collectors.toList()));
    }

    @NoArgsConstructor
    @Setter
    static class OrderLineItemRequestDto {

        private Long menuId;
        private Integer quantity;

        public CreateOrderLineItemDto toCreateOrderLineItemDto() {
            return new CreateOrderLineItemDto(menuId, quantity);
        }
    }
}
