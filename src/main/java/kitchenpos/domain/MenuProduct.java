package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;
    @Column(nullable = false)
    private Long productId;
    @Column(nullable = false)
    private Long quantity;

    public MenuProduct() {
    }

    private MenuProduct(Builder builder) {
        this.id = builder.id;
        this.menu = builder.menu;
        this.productId = builder.productId;
        this.quantity = builder.quantity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void updateMenu(Menu menu) {
        this.menu = menu;
    }

    public static class Builder {
        private Long id;
        private Menu menu;
        private Long productId;
        private long quantity;

        private Builder() {
        }

        public Builder of(MenuProduct menuProduct) {
            this.id = menuProduct.id;
            this.menu = menuProduct.menu;
            this.productId = menuProduct.productId;
            this.quantity = menuProduct.quantity;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder menu(Menu menu) {
            this.menu = menu;
            return this;
        }

        public Builder productId(Long productId) {
            this.productId = productId;
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

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
