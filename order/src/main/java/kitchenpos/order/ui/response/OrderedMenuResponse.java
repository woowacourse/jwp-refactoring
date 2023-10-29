package kitchenpos.order.ui.response;

import java.math.BigDecimal;

public class OrderedMenuResponse {
    private final Long id;
    private final String name;
    private final BigDecimal price;

    public OrderedMenuResponse(final Long id,
                               final String name,
                               final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "OrderedMenuResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
