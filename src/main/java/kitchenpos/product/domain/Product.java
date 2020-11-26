package kitchenpos.product.domain;

import kitchenpos.generic.Price;
import org.springframework.util.StringUtils;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    public Product() {
    }

    public Product(String name, Long price) {
        this.name = name;
        this.price = Price.of(price);
        validate();
    }

    Product(Long id, String name, Long price) {
        this(name, price);
        this.id = id;
    }

    private void validate() {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException(String.format("%s : 올바르지 않은 이름입니다.", name));
        }
    }

    public boolean isSameId(Long id) {
        return Objects.equals(this.id, id);
    }

    public Price calculatePrice(Long quantity) {
        return this.price.multiply(quantity);
    }

    public Long getId() {
        return id;
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
}
