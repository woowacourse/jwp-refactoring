package kitchenpos.application.dto.result;

import kitchenpos.domain.OrderLineItem;

public class MenuInOrderResult {

    private final Long seq;
    private final MenuResult menuResult;
    private final Long quantity;

    public MenuInOrderResult(final Long seq, final MenuResult menuResult, final Long quantity) {
        this.seq = seq;
        this.menuResult = menuResult;
        this.quantity = quantity;
    }

    public static MenuInOrderResult from(final OrderLineItem orderLineItems) {
        return new MenuInOrderResult(
                orderLineItems.getSeq(),
                MenuResult.from(orderLineItems.getMenu()),
                orderLineItems.getQuantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public MenuResult getMenuResult() {
        return menuResult;
    }

    public Long getQuantity() {
        return quantity;
    }
}
