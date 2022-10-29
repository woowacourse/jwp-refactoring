package kitchenpos.domain;

import java.math.BigDecimal;

public class MenuProduct implements Entity {

    private Long seq;
    private Long menuId;
    private Long productId;
    private Product product;
    private long quantity;

    public MenuProduct(final Long menuId,
                       final Product product,
                       final long quantity) {
        this(null, menuId, product.getId(), quantity);
        this.product = product;
    }

    public MenuProduct(final Long seq,
                       final Long menuId,
                       final Long productId,
                       final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public BigDecimal amount() {
        return BigDecimal.valueOf(quantity).multiply(product.getPrice());
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

    public Product getProduct() {
        return product;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean isNew() {
        return seq == null;
    }

    @Override
    public void validateOnCreate() {
    }
}
