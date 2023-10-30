package kitchenpos.order.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class ProductSnapshot {
    
    private String name;
    
    private BigDecimal price;
    
    protected ProductSnapshot() {
    }
    
    public static ProductSnapshot from(final Product product) {
        return new ProductSnapshot(product.getName(),
                product.getProductPrice().getPrice());
    }
    
    public ProductSnapshot(final String name,
                           final BigDecimal price) {
        this.name = name;
        this.price = price;
    }
    
    public String getName() {
        return name;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setPrice(final BigDecimal price) {
        this.price = price;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductSnapshot that = (ProductSnapshot) o;
        return name.equals(that.name) && price.equals(that.price);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
