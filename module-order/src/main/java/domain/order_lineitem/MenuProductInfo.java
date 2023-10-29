package domain.order_lineitem;

import java.math.BigDecimal;
import javax.persistence.Embeddable;

@Embeddable
public class MenuProductInfo {

    private final String name;
    private final BigDecimal price;
    private final Long quantity;

    protected MenuProductInfo() {
        this.name = null;
        this.price = null;
        this.quantity = null;
    }

    public MenuProductInfo(final String name, final BigDecimal price, final Long quantity) {
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

    public Long getQuantity() {
        return quantity;
    }
}
