package kitchenpos.domain;

import java.math.BigDecimal;

public class MenuProduct {

    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final long quantity;
    private BigDecimal price;

    public MenuProduct(final Long seq, final Long menuId, final Long productId, final long quantity, final BigDecimal price) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public MenuProduct(final Long seq, final Long menuId, final Long productId, final long quantity) {
        this(seq, menuId, productId, quantity, null);
    }

    public MenuProduct(final Long menuId, final Long productId, final long quantity) {
        this(null, menuId, productId, quantity, null);
    }

    public MenuProduct(final Long productId, final long quantity) {
        this(null, null, productId, quantity, null);
    }

    public MenuProduct(final Long productId, final long quantity,final BigDecimal price) {
        this(null, null, productId, quantity, price);
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

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
