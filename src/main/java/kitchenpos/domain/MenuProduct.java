package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private Long quantity;

    public MenuProduct() {
    }

    public MenuProduct(Builder builder) {
        this.seq = builder.seq;
        this.menu = builder.menu;
        this.product = builder.product;
        this.quantity = builder.quantity;
    }

    public void registerMenu(Menu menu) {
        this.menu = menu;
    }

    public BigDecimal calculatePrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public Product getProduct() {
        return product;
    }

    public Long getProductId() {
        return product.getId();
    }

    public static class Builder {
        private Long seq;
        private Menu menu;
        private Product product;
        private Long quantity;

        public Builder() {
        }

        public Builder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder menu(Menu menu) {
            this.menu = menu;
            return this;
        }

        public Builder product(Product product) {
            this.product = product;
            return this;
        }

        public Builder quantity(Long quantity) {
            this.quantity = quantity;
            return this;
        }

        public MenuProduct build() {
            return new MenuProduct(this);
        }
    }
}
