package kitchenpos.product.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.product.application.dto.MenuPrice;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private MenuPrice price;

    protected Product() {
    }

    private Product(String name, MenuPrice price) {
        this.name = name;
        this.price = price;
    }

    public long calculatePrice(long quantity) {
        return price.getValue()
                .multiply(BigDecimal.valueOf(quantity))
                .longValue();
    }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public static class ProductBuilder {

        private String name;
        private MenuPrice price;

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder price(long price) {
            this.price = MenuPrice.of(price);
            return this;
        }

        public Product build() {
            return new Product(name, price);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }
}
