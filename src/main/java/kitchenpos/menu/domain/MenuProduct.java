package kitchenpos.menu.domain;

import java.math.BigDecimal;

public class MenuProduct {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;
    private BigDecimal price;

    public MenuProduct(final Long productId, final long quantity, final BigDecimal price) {
        this(null, null, productId, quantity, price);
    }

    public MenuProduct(final Long productId, final long quantity) {
        this(null, null, productId, quantity, null);
    }

    public MenuProduct(final Long seq, final Long menuId, final Long productId,
                       final long quantity, final BigDecimal price) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public BigDecimal getAmount() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
