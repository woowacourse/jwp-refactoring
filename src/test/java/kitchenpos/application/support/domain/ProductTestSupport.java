package kitchenpos.application.support.domain;

import java.math.BigDecimal;
import java.util.UUID;
import kitchenpos.application.dto.request.ProductCreateRequest;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;

public class ProductTestSupport {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String name = "상품 이름" + UUID.randomUUID().toString().substring(0, 5);
        private BigDecimal price = new BigDecimal("3000");

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder price(final BigDecimal price) {
            this.price = price;
            return this;
        }

        public Product build() {
            return new Product(name, Price.from(price));
        }

        public ProductCreateRequest buildToProductCreateRequest() {
            return new ProductCreateRequest(name, price);
        }
    }
}
