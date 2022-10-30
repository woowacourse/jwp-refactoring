package kitchenpos.domain;

import java.math.BigDecimal;

public class MenuProduct {
    private final Long seq;
    private Long menuId;
    private final Long productId;
    private final long quantity;

    public MenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(Long productId, long quantity) {
        this(null, null, productId, quantity);
    }

    public void updateMenuId(final Long menuId) {
        this.menuId = menuId;
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

    public BigDecimal getSum(Product product) {
        return BigDecimal.valueOf(quantity).multiply(product.getPrice());
    }
}
