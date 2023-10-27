package kitchenpos.product;

import kitchenpos.common.Price;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
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
    @AttributeOverride(
            name = "amount",
            column = @Column(name = "price", precision = 19, scale = 2, nullable = false)
    )
    private Price price;

    public Product() {
    }

    public Product(Long id, String name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(String name, Price price) {
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price priceFor(long quantity) {
        return price.multiply(quantity);
    }

    public Price getPrice() {
        return price;
    }
}
