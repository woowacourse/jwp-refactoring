package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private Long id;
    private String name;
    private BigDecimal price;

    public Product(){
    }

    public Product(String name, BigDecimal price) {
        this(null, name, price);
    }

    public Product(Long id, String name, BigDecimal price) {
        validatePositive(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validatePositive(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("[ERROR] 가격은 없거나 음수일 수가 없습니다.");
        }
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public BigDecimal multiplyPrice(long quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
