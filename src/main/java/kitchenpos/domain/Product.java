package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 메뉴를 관리하는 기준이 되는 데이터
 */
public class Product {
    private final Long id;
    private final String name;
    private final BigDecimal price;

    public Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public boolean hasInvalidPrice() {
        return Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
