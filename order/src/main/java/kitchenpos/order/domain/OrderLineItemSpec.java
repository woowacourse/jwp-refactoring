package kitchenpos.order.domain;

import javax.persistence.Embeddable;
import kitchenpos.common.Price;

@Embeddable
public class OrderLineItemSpec {

    private String name;
    private Price price;

    protected OrderLineItemSpec() {
    }

    public OrderLineItemSpec(final String name, final Price price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
