package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "menu_product_id")
    private Long id;
    @Column
    private Long menuId;
    @Column(nullable = false)
    private Long productId;
    @Column(nullable = false)
    private Long quantity;

    public MenuProduct() {
    }

    private MenuProduct(Builder builder) {
        this.id = builder.id;
        this.menuId = builder.menuId;
        this.productId = builder.productId;
        this.quantity = builder.quantity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void updateMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public static class Builder {
        private Long id;
        private Long menuId;
        private Long productId;
        private long quantity;

        private Builder() {
        }

        public Builder of(MenuProduct menuProduct) {
            this.id = menuProduct.id;
            this.menuId = menuProduct.menuId;
            this.productId = menuProduct.productId;
            this.quantity = menuProduct.quantity;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder menuId(Long menuId) {
            this.menuId = menuId;
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
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
