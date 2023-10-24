package kitchenpos.order.domain;

import java.math.BigDecimal;
import javax.persistence.Embeddable;
import kitchenpos.menu.domain.MenuProduct;

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

    public static MenuProductSnapShot from(MenuProduct menuProduct) {
        return new MenuProductSnapShot(
                menuProduct.getProduct().getName(),
                menuProduct.getProduct().getPrice(),
                menuProduct.getQuantity()
        );
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
