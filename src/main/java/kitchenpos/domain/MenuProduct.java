package kitchenpos.domain;

import java.util.Objects;

public class MenuProduct {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    private MenuProduct() {
    }

    public MenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        validate(menuId, productId, quantity);
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(Long menuId, Long productId, long quantity) {
        this(null, menuId, productId, quantity);
    }

    private void validate(Long menuId, Long productId, long quantity) {
        validateProductId(productId);
        validateMenuId(menuId);
        validateQuantity(quantity);
    }

    private void validateProductId(Long productId) {
        if (Objects.isNull(productId)) {
            throw new IllegalArgumentException("productId of menuProduct must not be null.");
        }
    }

    private void validateMenuId(Long menuId) {
        if (Objects.isNull(menuId)) {
            throw new IllegalArgumentException("productId of menuProduct must not be null.");
        }
    }

    private void validateQuantity(Long quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                "The quantity of the menuProduct must be an integer greater than 1.");
        }
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
