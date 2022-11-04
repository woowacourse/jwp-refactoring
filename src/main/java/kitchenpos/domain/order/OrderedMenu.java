package kitchenpos.domain.order;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import kitchenpos.domain.common.Price;

@Embeddable
public class OrderedMenu {

    @Column(name = "menu_name", nullable = false)
    private String name;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "menu_price"))
    private Price price;

    protected OrderedMenu() {
    }

    public OrderedMenu(final String name, final Price price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderedMenu)) {
            return false;
        }
        OrderedMenu that = (OrderedMenu) o;
        return Objects.equals(name, that.name) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
