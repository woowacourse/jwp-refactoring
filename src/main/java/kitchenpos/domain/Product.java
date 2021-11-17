package kitchenpos.domain;

import java.math.BigDecimal;

public class Product {

    private Long id;
    private String name;
    private BigDecimal price;

    public Product() {
    }

    private Product(ProductBuilder productBuilder) {
        this.id = productBuilder.id;
        this.name = productBuilder.name;
        this.price = productBuilder.price;
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

    public static class ProductBuilder {

        private Long id;
        private String name;
        private BigDecimal price;

        public ProductBuilder setId(Long id) {
            this.id = id;
            return this;
        }

        public ProductBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }
}
