package kitchenpos.common.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class TProduct {

    public static Product 후라이드() {
        return builder()
            .name("후라이드")
            .price(BigDecimal.valueOf(10000))
            .build();
    }

    public static Product 강정치킨() {
        return builder()
            .name("강정치킨")
            .price(BigDecimal.valueOf(17000))
            .build();
    }

    public static Builder builder() {
        return new Builder();
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
            this.price = price;
            return this;
        }

        public Product build() {
            Product product = new Product();
            product.setId(id);
            product.setName(name);
            product.setPrice(price);
            return product;
        }
    }
}
