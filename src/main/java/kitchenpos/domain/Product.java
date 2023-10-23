package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Product {
    
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    
    private String name;
    
    @Embedded
    private ProductPrice price;
    
    public Product(final String name, final ProductPrice price) {
        this(null, name, price);
    }
    
    public Product(final Long id,
                   final String name,
                   final ProductPrice price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
    
    public ProductPrice multiplyQuantity(final long quantity) {
        return this.price.multiply(BigDecimal.valueOf(quantity));
    }
    
    public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public ProductPrice getPrice() {
        return price;
    }
}
