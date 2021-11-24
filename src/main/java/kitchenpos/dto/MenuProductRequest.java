package kitchenpos.dto;

import kitchenpos.domain.MenuProduct;

public class MenuProductRequest {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductRequest(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toMenuProduct() {
        return new MenuProduct(seq, menuId, productId, quantity);
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
