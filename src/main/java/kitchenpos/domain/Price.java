package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {
    
    private final BigDecimal price;
    
    public Price(final BigDecimal price) {
        this.price = price;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price1 = (Price) o;
        return price.equals(price1.price);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
    
    @Override
    public String toString() {
        return "Price{" +
                "price=" + price +
                '}';
    }
}
