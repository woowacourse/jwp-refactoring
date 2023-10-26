package kitchenpos.menu.application.dto.response;

import kitchenpos.menu.domain.MenuProduct;

public class MenuProductQueryResponse {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public MenuProductQueryResponse(final Long seq, final Long menuId, final Long productId,
                                    final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductQueryResponse from(final Long menuId, final MenuProduct menuProduct) {
        return new MenuProductQueryResponse(menuProduct.getSeq(), menuId,
                menuProduct.getProduct().getId(), menuProduct.getQuantity());
    }

    public MenuProductQueryResponse() {
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
