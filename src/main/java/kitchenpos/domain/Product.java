package kitchenpos.domain;

import kitchenpos.domain.vo.Price;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Product {

    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;

    @Column
    private String name;

    @Embedded
    private Price price;

    protected Product() {
    }

    private Product(final Long id, final String name, final Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(final String name, final Price price) {
        this(null, name, price);
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
