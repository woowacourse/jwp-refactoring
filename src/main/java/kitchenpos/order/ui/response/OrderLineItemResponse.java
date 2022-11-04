package kitchenpos.order.ui.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;

public class OrderLineItemResponse {

    private final String name;
    private final BigDecimal price;
    private final long quantity;

    @JsonCreator
    public OrderLineItemResponse(final String name, final BigDecimal price, final long quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }
}
