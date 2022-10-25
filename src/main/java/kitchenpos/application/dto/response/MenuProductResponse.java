package kitchenpos.application.dto.response;

import kitchenpos.domain.MenuProduct;

public record MenuProductResponse(Long seq,
                                  Long menuId,
                                  Long productId,
                                  long quantity) {

    public static MenuProductResponse from(final MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getMenuId(), menuProduct.getProductId(),
                menuProduct.getQuantity());
    }
}
