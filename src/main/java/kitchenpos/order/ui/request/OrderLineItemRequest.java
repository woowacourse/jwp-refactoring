package kitchenpos.order.ui.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class OrderLineItemRequest {

    @NotNull(message = "메뉴는 null 일 수 없습니다.")
    private Long menuId;

    @Min(value = 0, message = "최소 개수는 0개 이상입니다.")
    private Long quantity;

    protected OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menuId, Long quantity) {
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
