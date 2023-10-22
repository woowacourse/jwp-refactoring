package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.InvalidQuantityException;

@Embeddable
public class OrderLineItemQuantity {

    @Column(name = "quantity", nullable = false)
    private Long value;

    public OrderLineItemQuantity() {
    }

    public OrderLineItemQuantity(final Long value) {
        validate(value);
        this.value = value;
    }

    private void validate(final Long value) {
        if (value == null || value < 0) {
            throw new InvalidQuantityException("주문 상품 수량은 0 이상이어야 합니다.");
        }
    }

    public long getValue() {
        return value;
    }
}
