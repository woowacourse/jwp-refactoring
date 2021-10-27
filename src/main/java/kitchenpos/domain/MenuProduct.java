package kitchenpos.domain;

public class MenuProduct {
    private Long seq;
    private Long menuId;
    private Long productId;
    private Long quantity;

    public MenuProduct(Long productId, Long quantity) {
        this(null, null, productId, quantity);
    }

    public MenuProduct(Long menuId, Long productId, Long quantity) {
        this(null, menuId, productId, quantity);
    }

    public MenuProduct(Long seq, Long menuId, Long productId, Long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
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

    public Long getQuantity() {
        return quantity;
    }

    public void addMenuId(Long menuId) {
        this.menuId = menuId;
    }
}
