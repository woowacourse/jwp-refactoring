package kitchenpos.domain;

import java.math.BigDecimal;

public class MenuProduct {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;
    private BigDecimal price = BigDecimal.ZERO;

    public MenuProduct() {
    }

    public MenuProduct(final Long productId, final long quantity, final BigDecimal price) {
        this(null, productId, quantity);
        this.price = price;
    }

    public MenuProduct(final Long menuId, final Long productId, final long quantity) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(final Long seq, final Long menuId, final Long productId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }

    public void setProductId(final Long productId) {
        this.productId = productId;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public BigDecimal getAmount() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
