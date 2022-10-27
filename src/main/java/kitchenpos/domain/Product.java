package kitchenpos.domain;

import java.math.BigDecimal;
import kitchenpos.domain.vo.Price;

public class Product {

    private final Long id;
    private final String name;
    private final Price price;

    public Product(final Long id, final String name, final Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public static class Builder {

        private Long id;
        private String name;
        private Price price;

        public Builder id(final Long id) {
            this.id = id;
            return this;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder price(final BigDecimal price) {
            this.price = new Price(price);
            return this;
        }

        public Product build() {
            return new Product(id, name, price);
        }
    }
}
