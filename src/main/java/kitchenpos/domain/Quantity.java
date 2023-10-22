package kitchenpos.domain;

import java.util.Objects;

public class Quantity {
    
    private final long quantity;
    
    public Quantity(final long quantity) {
        this.quantity = quantity;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity quantity1 = (Quantity) o;
        return quantity == quantity1.quantity;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
    
    @Override
    public String toString() {
        return "Quantity{" +
                "quantity=" + quantity +
                '}';
    }
}
