package kitchenpos.order.domain;

import java.math.BigDecimal;
import kitchenpos.common.Price;

public class OrderedMenu {

    private final Long id;
    private final String name;
    private final Price price;

    public OrderedMenu(final Long id, final String name, final Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public OrderedMenu(final String name, final Price price) {
        this(null, name, price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }
}
