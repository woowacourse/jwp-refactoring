package kitchenpos.domain;

public class MenuProduct {

    private Long seq;
    private Long menuId;
    private Product product;
    private long quantity;

    public MenuProduct() {
    }

    private MenuProduct(final Long seq, final Long menuId, final Product product, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct(final Long menuId, final Product product, final long quantity) {
        this(null, menuId, product, quantity);
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

    public void setProductId(final Long productId) {
        this.product.setId(productId);
    }

    public Product getProduct() {
        return product;
    }

    public Long getProductId() {
        return product.getId();
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}
