package kitchenpos.menu.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import kitchenpos.product.domain.Price;

@Embeddable
public class UpdatableMenuInfo {

    @Embedded
    private Price price;
    @Column(name = "name")
    private String name;

    protected UpdatableMenuInfo() {
    }

    public UpdatableMenuInfo(final Price price, final String name) {
        this.price = price;
        this.name = name;
    }

    public Price getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UpdatableMenuInfo that = (UpdatableMenuInfo) o;
        return Objects.equals(price, that.price) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price, name);
    }
}
