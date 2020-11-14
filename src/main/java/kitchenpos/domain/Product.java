package kitchenpos.domain;

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
    private ProductPrice productPrice;

    public Product() {
    }

    public Product(String name, ProductPrice productPrice) {
        this.name = name;
        this.productPrice = productPrice;
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
