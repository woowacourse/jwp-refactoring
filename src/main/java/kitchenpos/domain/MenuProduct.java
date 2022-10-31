package kitchenpos.domain;

import java.math.BigDecimal;

public class MenuProduct {

    private Long seq;
    private Long menuId;
    private final Long productId;
    private final long quantity;

    private BigDecimal price;

    public MenuProduct(final Long seq, final Long menuId, final Long productId, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(final Long productId, final long quantity, final BigDecimal price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getSeq() {
        return seq;
    }

    public BigDecimal getAmount() {
        return price.multiply(BigDecimal.valueOf(quantity));
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

    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }
}
