package kitchenpos.menu.application.dto;

import kitchenpos.menu.domain.MenuProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MenuProductResponse {
    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final long quantity;

    public static MenuProductResponse from(final MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuProduct.getMenuId(), menuProduct.getProductId(),
            menuProduct.getQuantity());
    }
}
