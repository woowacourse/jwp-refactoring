package kitchenpos.menu.application.dto.response;

import kitchenpos.menu.domain.MenuProduct;

public record MenuProductResponse(Long seq,
                                  Long menuId,
                                  Long productId,
                                  long quantity) {

    public static MenuProductResponse from(final MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getMenuId(), menuProduct.getProductId(),
                menuProduct.getQuantity());
    }
}
