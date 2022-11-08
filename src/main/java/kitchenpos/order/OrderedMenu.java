package kitchenpos.order;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderedMenu {

    @Column(name = "menu_id")
    private Long id;
    @Column(name = "menu_name")
    private String name;
    @Column(name = "menu_price")
    private BigDecimal price;

    public OrderedMenu(final Long id, final String name, final BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderedMenu that = (OrderedMenu) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
                && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }

    protected OrderedMenu() {
    }
}
