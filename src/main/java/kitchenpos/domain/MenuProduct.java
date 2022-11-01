package kitchenpos.domain;

import java.math.BigDecimal;

public class MenuProduct {

    private Long seq;
    private Long menuId;
    private Long productId;
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

    public BigDecimal calculateAmount(final Product product) {
        if (product.isSameProductId(productId)) {
            return product.getPrice().multiply(BigDecimal.valueOf(quantity));
        }
        return BigDecimal.ZERO;
    }
}
