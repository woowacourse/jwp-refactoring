package kitchenpos.domain;

import java.math.BigDecimal;

public class Product {
    private final Long id;
    private final String name;
    private final BigDecimal price;

    private Product(Long id, String name, BigDecimal price) {
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validatePrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
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

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public static final class ProductBuilder {
        private Long id;
        private String name;
        private BigDecimal price;

        private ProductBuilder() {
        }

        public ProductBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Product build() {
            return new Product(id, name, price);
        }
    }
}
