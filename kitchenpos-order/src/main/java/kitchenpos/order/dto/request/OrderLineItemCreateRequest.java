package kitchenpos.order.dto.request;

import java.util.Objects;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class OrderLineItemCreateRequest {

    @NotNull(message = "메뉴 id를 입력해 주세요.")
    private final Long menuId;

    @NotNull(message = "수량을 입력해 주세요")
    @Min(1)
    private final long quantity;

    public OrderLineItemCreateRequest(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderLineItemCreateRequest that = (OrderLineItemCreateRequest) o;
        return Objects.equals(menuId, that.menuId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId);
    }
}
