package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

import static io.micrometer.core.instrument.util.StringUtils.isBlank;
import static java.util.Objects.isNull;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;

    protected Product() {
    }

    public Product(final Long id, final String name, final BigDecimal price) {
        if (isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("상품의 금액이 존재하지 않거나 음수입니다.");
        }
        if (isBlank(name)) {
            throw new IllegalArgumentException("상품의 이름이 존재하지 않습니다.");
        }
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(final String name, final BigDecimal price) {
        this(null, name, price);
    }

    public BigDecimal calculatePrice(final long quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
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
