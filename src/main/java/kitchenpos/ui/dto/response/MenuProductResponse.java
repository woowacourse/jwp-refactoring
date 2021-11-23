package kitchenpos.ui.dto.response;

import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {

    private Long seq;
    private Long menuId;
    private Long productId;
    private Long quantity;

    private MenuProductResponse() {
    }

    private MenuProductResponse(MenuProduct menuProduct) {
        this(
                menuProduct.getSeq(),
                menuProduct.getMenu().getId(),
                menuProduct.getProduct().getId(),
                menuProduct.getQuantity()
        );
    }

    private MenuProductResponse(
            Long seq,
            Long menuId,
            Long productId,
            Long quantity
    ) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse create(MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct);
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
}
