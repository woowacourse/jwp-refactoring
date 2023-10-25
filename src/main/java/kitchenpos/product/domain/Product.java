package kitchenpos.product.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static io.micrometer.core.instrument.util.StringUtils.isBlank;
import static java.util.Objects.isNull;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Price price;

    protected Product() {
    }

    public Product(final Long id, final String name, final Price price) {
        if (isBlank(name)) {
            throw new IllegalArgumentException("상품의 이름이 존재하지 않습니다.");
        }
        if (isNull(price)) {
            throw new IllegalArgumentException("상품 금액이 필요합니다.");
        }
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(final String name, final Price price) {
        this(null, name, price);
    }

    public Price calculatePrice(final long quantity) {
        return price.multiply(quantity);
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
