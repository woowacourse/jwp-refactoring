package kitchenpos.application.dtos;

import kitchenpos.domain.OrderLineItem;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class OrderLineItemRequest {
    private Long menuId;
    private Long quantity;

    public OrderLineItemRequest(OrderLineItem orderLineItem){
        this.menuId = orderLineItem.getMenuId();
        this.quantity = orderLineItem.getQuantity();
    }
}
