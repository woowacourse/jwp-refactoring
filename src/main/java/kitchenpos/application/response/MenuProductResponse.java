package kitchenpos.application.response;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {

    private Long seq;
    private Long menuId;
    private Long productId;
    private long quantity;

    public static MenuProductResponse from(MenuProduct menuProduct) {
        final MenuProductResponse menuProductResponse = new MenuProductResponse();
        menuProductResponse.seq = menuProduct.getSeq();
        menuProductResponse.menuId = menuProduct.getMenu().getId();
        menuProductResponse.productId = menuProduct.getProduct().getId();
        menuProductResponse.quantity = menuProduct.getQuantity();
        return menuProductResponse;
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
