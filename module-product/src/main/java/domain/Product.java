package domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import vo.Price;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    @Embedded
    private final ProductName productName;
    @Embedded
    private final Price price;

    public Product() {
        id = null;
        productName = null;
        price = null;
    }

    public Product(final ProductName productName, final Price price) {
        this.id = null;
        this.productName = productName;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public ProductName getProductName() {
        return productName;
    }

    public Price getProductPrice() {
        return price;
    }
}
