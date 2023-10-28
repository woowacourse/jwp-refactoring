package kitchenpos.menu.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;
    private String name;
    private BigDecimal price;
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long seq, String name, BigDecimal price, long quantity) {
        this.seq = seq;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public BigDecimal calculatePriceSum() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public Long getSeq() {
        return seq;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }

    public static class Builder {

        private Long seq;
        private String name;
        private BigDecimal price;
        private long quantity;

        public Builder seq(Long seq) {
            this.seq = seq;
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

        public Builder quantity(long quantity) {
            this.quantity = quantity;
            return this;
        }

        public MenuProduct build() {
            return new MenuProduct(seq, name, price, quantity);
        }
    }
}
