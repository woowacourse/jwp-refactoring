package kitchenpos.order.domain;

import javax.persistence.Embeddable;

@Embeddable
public class OrderLineItemName {

    private String name;

    protected OrderLineItemName() {
    }

    public OrderLineItemName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
