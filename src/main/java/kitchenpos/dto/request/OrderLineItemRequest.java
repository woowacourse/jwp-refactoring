package kitchenpos.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class OrderLineItemRequest {

    @NotNull(message = "메뉴 id를 입력해 주세요.")
    private final Long menuId;

    @NotNull(message = "수량을 입력해 주세요")
    @Min(1)
    private final long quantity;

    public OrderLineItemRequest(final Long menuId, final long quantity) {
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
