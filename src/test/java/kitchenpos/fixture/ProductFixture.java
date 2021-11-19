package kitchenpos.fixture;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class ProductFixture {

    public static Product productForCreate(String name, Long price) {
        return productForCreate(name, BigDecimal.valueOf(price));
    }

    public static Product productForCreate(String name, BigDecimal price) {
        return builder()
                .name(name)
                .price(price)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long id;
        private String name;
        private BigDecimal price;

        public Product build() {
            Product product = new Product();
            product.setId(id);
            product.setName(name);
            product.setPrice(price);
            return product;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder price(Long price) {
            this.price = BigDecimal.valueOf(price);
            return this;
        }
    }
}
