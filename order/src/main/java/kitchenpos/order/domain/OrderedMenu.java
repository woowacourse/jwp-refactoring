package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class OrderedMenu {
    private Long menuId;
    @Column(name = "menu_name")
    private String name;
    @Column(name = "menu_price")
    private BigDecimal price;

    protected OrderedMenu() {
    }

    public OrderedMenu(final Long menuId,
                       final String name,
                       final BigDecimal price) {
        this.menuId = menuId;
        this.name = name;
        this.price = price;
    }

    public Long getMenuId() {
        return menuId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderedMenu that = (OrderedMenu) o;
        return Objects.equals(menuId, that.menuId)
                && Objects.equals(name, that.name)
                && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, name, price);
    }

    @Override
    public String toString() {
        return "OrderedMenu{" +
                "menuId=" + menuId +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
