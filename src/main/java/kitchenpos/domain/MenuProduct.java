package kitchenpos.domain;

public class MenuProduct {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(final Long seq, final Long menuId, final Long productId, final long quantity) {
        validateQuantity(quantity);
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(final Long menuId, final Long productId, final long quantity) {
        this(null, menuId, productId, quantity);
    }

    private void validateQuantity(final long quantity) {
        // TODO: MenuProduct readme 작성하기 (validate)
        if (quantity <= 0) {
            throw new IllegalArgumentException("메뉴 상품의 수량은 1개 이상이어야 합니다.");
        }
    }

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
}
