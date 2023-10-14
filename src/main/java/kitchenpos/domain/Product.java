package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private Long id;
    private String name;
    private BigDecimal price;

    private Product() {
    }

    public static class Builder {
        private Long id;
        private String name;
        private BigDecimal price;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(BigDecimal price) {
            validate(price);
            this.price = price;
            return this;
        }

        public Product build() {
            Product product = new Product();
            product.id = id;
            product.name = name;
            product.price = price;
            return product;
        }

        private void validate(BigDecimal price) {
            if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Price must be greater than or equal to zero.");
            }
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
}
