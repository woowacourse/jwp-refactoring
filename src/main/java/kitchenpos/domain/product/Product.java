package kitchenpos.domain.product;

import kitchenpos.config.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

@AttributeOverride(name = "id", column = @Column(name = "product_id"))
@Entity
public class Product extends BaseEntity {
    private String name;
    @Embedded
    private ProductPrice productPrice;

    public Product() {
    }

    public Product(Long id, String name, ProductPrice productPrice) {
        this.id = id;
        this.name = name;
        this.productPrice = productPrice;
    }

    public Product(String name, ProductPrice productPrice) {
        this(null, name, productPrice);
    }

    public String getName() {
        return name;
    }

    public ProductPrice getProductPrice() {
        return productPrice;
    }
}
