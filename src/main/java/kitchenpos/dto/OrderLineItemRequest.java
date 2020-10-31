package kitchenpos.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemRequest {
    @NotNull(message = "메뉴가 비어있을 수 없습니다.")
    private final Long menuId;
    @Min(value = 1, message = "주문수량이 1 미만일수 없습니다.")
    private final long quantity;

    public OrderLineItemRequest(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toEntity(final Menu menu, final Order order) {
        return new OrderLineItem(order, menu, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
