package kitchenpos.domain.order;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderLineItemQuantity {

    @Column
    private long quantity;

    protected OrderLineItemQuantity() {
    }

    protected OrderLineItemQuantity(long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private void validate(long quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException(
                "OrderLineItem quantity는 1개보다 작을 수 없습니다 : {" + quantity + "개}");
        }
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderLineItemQuantity that = (OrderLineItemQuantity) o;
        return quantity == that.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }

    @Override
    public String toString() {
        return "OrderLineItemQuantity{" +
            "quantity=" + quantity +
            '}';
    }
}
