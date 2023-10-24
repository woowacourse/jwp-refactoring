package kitchenpos.domain.order;

import kitchenpos.exception.OrderLineItemQuantityException;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class OrderLineItemQuantity {

    private Long quantity;

    protected OrderLineItemQuantity() {
    }

    public OrderLineItemQuantity(Long quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    private void validateQuantity(Long quantity) {
        if (isNull(quantity) || isNegative(quantity)) {
            throw new OrderLineItemQuantityException("주문메뉴 수량은 0 이상이어야 합니다.");
        }
    }

    private boolean isNull(Long quantity) {
        return Objects.isNull(quantity);
    }

    private boolean isNegative(Long quantity) {
        return quantity < 0;
    }

    public Long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLineItemQuantity that = (OrderLineItemQuantity) o;
        return Objects.equals(getQuantity(), that.getQuantity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuantity());
    }
}
