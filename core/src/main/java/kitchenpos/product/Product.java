package kitchenpos.product;

import kitchenpos.common.Price;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    protected Product() {

    }

    public Product(final String name, final Price price) {
        this(null, name, price);
    }

    public Product(final Long id, final String name, final Price price) {
        validateProduct(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validateProduct(final Price price) {
        if (price == null) {
            throw new IllegalArgumentException(String.format("가격은 null 일 수 없습니다. 입력값 = %s", price));
        }
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
