package kitchenpos.menu.vo;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class ProductSpecification {

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    private ProductSpecification(String name, Price price) {
        this.name = name;
        this.price = price;
    }

    protected ProductSpecification() {
    }

    public static ProductSpecification from(String name, BigDecimal price) {
        return new ProductSpecification(name, Price.valueOf(price));
    }

    public Price calculateTotalPrice(Quantity quantity) {
        return price.multiply(quantity.getValue());
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
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
        ProductSpecification that = (ProductSpecification) o;
        return Objects.equals(name, that.name) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
