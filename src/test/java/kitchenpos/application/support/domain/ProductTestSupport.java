package kitchenpos.application.support.domain;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

public class ProductTestSupport {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private static Long autoCount = 0L;

        private Long id = ++autoCount;
        private String name = "상품 이름" + id;
        private BigDecimal price = new BigDecimal("3000");

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
            final var result = new Product();
            result.setId(id);
            result.setName(name);
            result.setPrice(price);
            return result;
        }
    }
}
