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
    private ProductPrice productPrice;
    
    public Product(final String name, final ProductPrice productPrice) {
        this(null, name, productPrice);
    }
    
    public Product(final Long id,
                   final String name,
                   final ProductPrice productPrice) {
        this.id = id;
        this.name = name;
        this.productPrice = productPrice;
    }
    
    public ProductPrice multiplyQuantity(final long quantity) {
        return this.productPrice.multiply(BigDecimal.valueOf(quantity));
    }
    
    public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public ProductPrice getProductPrice() {
        return productPrice;
    }
}
