package kitchenpos.order.domain.dto.vo;

import javax.persistence.Column;

public class OrderLineItemQuantity {

    private static final int MIN_QUANTITY = 0;

    @Column(name = "quantity")
    private long quantity;

    protected OrderLineItemQuantity() {
    }

    public OrderLineItemQuantity(long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private void validate(long quantity) {
        if (quantity < MIN_QUANTITY) {
            throw new IllegalArgumentException("수량은 0보다 작을 수 없습니다.");
        }
    }

    public long getValue() {
        return quantity;
    }
}
