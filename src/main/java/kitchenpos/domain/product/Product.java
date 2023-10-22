package kitchenpos.domain.product;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    @Embedded
    private final ProductName productName;
    @Embedded
    private final ProductPrice productPrice;

    public Product() {
        id = null;
        productName = null;
        productPrice = null;
    }

    public Product(final ProductName productName, final ProductPrice productPrice) {
        this.id = null;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    public Long getId() {
        return id;
    }

    public ProductName getProductName() {
        return productName;
    }

    public ProductPrice getProductPrice() {
        return productPrice;
    }
}
