package kitchenpos.order.ui.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class OrderLineItemRequest {

    @NotNull(message = "메뉴는 null 일 수 없습니다.")
    private Long menu;

    @Min(value = 0, message = "최소 개수는 0개 이상입니다.")
    private Long quantity;

    protected OrderLineItemRequest() {
    }

    public OrderLineItemRequest(Long menu, Long quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public Long getMenu() {
        return menu;
    }

    public Long getQuantity() {
        return quantity;
    }
}
