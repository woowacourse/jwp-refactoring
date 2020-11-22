package kitchenpos.domain.product;

import kitchenpos.util.ValidateUtil;

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

    protected Product() {
    }

    public Product(String name, ProductPrice productPrice) {
        this.name = name;
        this.productPrice = productPrice;
    }

    public static Product of(String name, ProductPrice productPrice) {
        ValidateUtil.validateNonNullAndNotEmpty(name);
        ValidateUtil.validateNonNull(productPrice);

        return new Product(name, productPrice);
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
