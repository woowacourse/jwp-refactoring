package kitchenpos.order.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderMenu {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false, scale = 2)
    private BigDecimal price;

    protected OrderMenu() {
    }

    public OrderMenu(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }
}
