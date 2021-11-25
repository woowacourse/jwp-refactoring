package kitchenpos.product.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.Price;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Price price;

    private String name;

    public Product(Long id, String name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(String name, Price price) {
        this(null, name, price);
    }

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}
