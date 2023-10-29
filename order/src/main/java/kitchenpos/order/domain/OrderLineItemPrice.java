package kitchenpos.order.domain;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class OrderLineItemPrice {

    private BigDecimal price;

    protected OrderLineItemPrice() {
    }

    public OrderLineItemPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
