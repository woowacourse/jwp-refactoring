package kitchenpos.application.request;

import kitchenpos.domain.MenuProduct;

public class MenuProductRequest {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    private MenuProductRequest() {
    }

    public MenuProductRequest(Long seq, Long menuId, Long productId, long quantity) {
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

    public long getQuantity() {
        return quantity;
    }

    public MenuProduct toEntity() {
        return new MenuProduct(seq, menuId, productId, quantity);
    }
}
