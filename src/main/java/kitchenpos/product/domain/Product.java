package kitchenpos.product.domain;

import org.springframework.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    public Product() {
    }

    public Product(String name, Long price) {
        this.name = name;
        this.price = BigDecimal.valueOf(price);
        validate();
    }

    private void validate() {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException(String.format("%s : 올바르지 않은 이름입니다.", name));
        }
    }

    public boolean isSameId(Long id) {
        return Objects.equals(this.id, id);
    }

    public BigDecimal calculatePrice(Long quantity) {
        return this.price.multiply(BigDecimal.valueOf(quantity));
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
}
