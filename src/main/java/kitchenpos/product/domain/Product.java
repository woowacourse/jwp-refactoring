package kitchenpos.product.domain;

import java.math.BigDecimal;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private ProductPrice productPrice;

    public Product() {
    }

    public Product(Long id, String name, BigDecimal productPrice) {
        this.id = id;
        this.name = name;
        this.productPrice = new ProductPrice(productPrice);
    }

    public Product(String name, BigDecimal productPrice) {
        this(null, name, productPrice);
    }

    public ProductPrice getProductPrice() {
        return productPrice;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
