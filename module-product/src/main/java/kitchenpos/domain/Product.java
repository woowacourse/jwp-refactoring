package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kitchenpos.vo.Price;
import kitchenpos.vo.Quantity;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    protected Product() {
    }

    public Product(String name, Price price) {
        this(null, name, price);
    }

    public Product(Long id, String name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Price calculateAmount(Quantity quantity) {
        return price.calculateAmount(quantity);
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Price price() {
        return price;
    }
}
