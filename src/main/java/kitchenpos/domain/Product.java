package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

    private static final String PRICE_ERROR_MESSAGE = "가격은 0 이상의 수여야 합니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private BigDecimal price;

    public Product() {
    }

    private Product(final Builder builder) {
        validatePrice(builder.price);
        this.id = builder.id;
        this.name = builder.name;
        this.price = builder.price;
    }

    public static Builder builder() {
        return new Builder();
    }

    private void validatePrice(final BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(PRICE_ERROR_MESSAGE);
        }
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

    public static class Builder {
        private Long id;
        private String name;
        private BigDecimal price;

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder price(final BigDecimal price) {
            this.price = price;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
