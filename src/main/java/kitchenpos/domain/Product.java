package kitchenpos.domain;

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

    @Embedded
    private Price price;

    private String name;

    private Product(String name, Price price) {
        this.name = name;
        this.price = price;
    }

    protected Product() {
    }

    public static Product of(String name, Double price) {
        return new Product(name, Price.from(price));
    }

    public Long getId() {
        return id;
    }

    public double getPrice() {
        return price.getValue();
    }

    public String getName() {
        return name;
    }
}
