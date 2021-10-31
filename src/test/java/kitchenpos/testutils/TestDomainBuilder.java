package kitchenpos.testutils;

import kitchenpos.domain.Product;

import java.math.BigDecimal;

public class TestDomainBuilder {

    public static ProductBuilder productBuilder() {
        return new ProductBuilder();
    }

    public static class ProductBuilder {
        private Long id;
        private String name;
        private BigDecimal price;

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
            Product product = new Product();
            product.setId(id);
            product.setName(name);
            product.setPrice(price);
            return product;
        }
    }
}
