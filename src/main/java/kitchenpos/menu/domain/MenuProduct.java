package kitchenpos.menu.domain;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;

public class MenuProduct {

    private Long seq;
    private Long menuId;
    private Long productId;

    private Product product;

    private long quantity;

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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(final Long productId) {
        this.productId = productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public void setProduct(final Product product) {
        this.product = product;
    }

    public String getProductName() {
        return product.getName();
    }

    public BigDecimal getPrice() {
        return product.getPrice();
    }
}
