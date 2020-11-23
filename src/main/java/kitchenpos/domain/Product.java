package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {

    private Long id;
    private String name;
    private Price price;

    private Product() {
    }

    public Product(Long id, String name, BigDecimal price) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.price = new Price(price);
    }

    public Product(String name, BigDecimal price) {
        this(null, name, price);
    }

    private void validateName(String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("상품의 이름이 누락되었습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }
}
