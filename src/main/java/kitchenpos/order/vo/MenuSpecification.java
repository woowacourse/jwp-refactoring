package kitchenpos.order.vo;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuSpecification {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    public MenuSpecification(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    protected MenuSpecification() {
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuSpecification that = (MenuSpecification) o;
        return Objects.equals(name, that.name) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
