package kitchenpos.menu.domain;

import java.math.BigDecimal;

public class MenuProduct {
    private Long seq;
    private Long productId;
    private long quantity;
    private BigDecimal price = BigDecimal.ZERO;

    public MenuProduct() {
    }

    public MenuProduct(final Long productId, final long quantity, final BigDecimal price) {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public MenuProduct(final Long productId, final long quantity) {
        this(productId, quantity, null);
    }

    public BigDecimal calculateAmount() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
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
}
