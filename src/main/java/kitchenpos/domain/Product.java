package kitchenpos.domain;

import javax.persistence.*;

@AttributeOverride(name = "id", column = @Column(name = "id"))
@Table(name = "product")
@Entity
public class Product extends BaseEntity {
    private String name;

    @Embedded
    private Price price;

    public Product() {
    }

    public Product(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
