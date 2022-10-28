package kitchenpos.domain;

import java.util.Objects;

public class MenuProduct {
    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        validateProductId(productId);
        validateQuantity(quantity);
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    private void validateQuantity(long quantity) {
        if (quantity < 1L) {
            throw new IllegalArgumentException();
        }
    }

    private void validateProductId(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException();
        }
    }

    public MenuProduct(Long productId, long quantity) {
        this(null, null, productId, quantity);
    }

    public MenuProduct changeMenuId(Long menuId) {
        return new MenuProduct(this.seq, menuId, this.productId, this.quantity);
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
}
