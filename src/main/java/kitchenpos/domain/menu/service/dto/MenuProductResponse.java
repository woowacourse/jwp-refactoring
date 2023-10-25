package kitchenpos.domain.menu.service.dto;

import kitchenpos.domain.menu.MenuProduct;

public class MenuProductResponse {

    private final Long seq;

    private final Long menuId;

    private final ProductResponse product;

    private final long quantity;

    public MenuProductResponse(final Long seq, final Long menuId, final ProductResponse product, final long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProductResponse toDto(final MenuProduct menuProduct) {
        final ProductResponse productResponse = ProductResponse.toDto(menuProduct.getProduct());
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getMenu().getId(), productResponse, menuProduct.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }
}
