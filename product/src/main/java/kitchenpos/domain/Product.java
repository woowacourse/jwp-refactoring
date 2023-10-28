package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.domain.vo.ProductName;
import kitchenpos.domain.vo.ProductPrice;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ProductName productName;

    @Embedded
    private ProductPrice productPrice;

    protected Product() {
    }

    public Product(String name, BigDecimal price) {
        this.productName = new ProductName(name);
        this.productPrice = new ProductPrice(price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return productName.getName();
    }

    public BigDecimal getPrice() {
        return productPrice.getPrice();
    }
}
