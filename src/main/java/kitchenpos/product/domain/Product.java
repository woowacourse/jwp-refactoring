package kitchenpos.product.domain;

import java.math.BigDecimal;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.element.Price;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Price price;

    public Product() {
    }

    private Product(Builder builder) {
        this.name = builder.name;
        this.price = builder.price;
        this.id = builder.id;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void updatePrice(Long price) {
        this.price = new Price(price);
    }

    public static class Builder {
        private Long id;
        private String name;
        private Price price;

        private Builder() {
        }

        public Builder of(Product product) {
            this.id = product.id;
            this.name = product.name;
            this.price = product.price;
            return this;
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
            this.price = new Price(price);
            return this;
        }

        public Builder price(Price price) {
            this.price = price;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

}
