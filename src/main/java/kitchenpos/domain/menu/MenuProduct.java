package kitchenpos.domain.menu;

import kitchenpos.domain.product.Product;

import java.math.BigDecimal;

public class MenuProduct {
    private Long seq;
    private Long menuId;
    private Product product;
    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(Long seq, Long menuId, Product product, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.product = product;
        this.quantity = quantity;
    }

    public BigDecimal calculateTotalPrice() {
        return product.multiplyWithQuantity(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}
