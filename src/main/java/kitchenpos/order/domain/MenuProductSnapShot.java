package kitchenpos.order.domain;

import java.math.BigDecimal;
import javax.persistence.Embeddable;

@Embeddable
public class MenuProductSnapShot {

    private String name;
    private BigDecimal price;
    private long quantity;

    protected MenuProductSnapShot() {
    }

    public MenuProductSnapShot(String name, BigDecimal price, long quantity) {
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
