package kitchenpos.domain;

import javax.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    public Product() {
    }

    public Product(String name, Long price) {
        this(null, name, price);
    }

    public Product(Long id, String name, Long price) {
        this.id = id;
        this.name = name;
        this.price = Price.from(price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price.longValue();
    }
}
