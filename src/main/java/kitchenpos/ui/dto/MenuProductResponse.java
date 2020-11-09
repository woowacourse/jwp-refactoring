package kitchenpos.ui.dto;

import kitchenpos.domain.MenuProduct;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MenuProductResponse {
    private final Long seq;
    private final Long menuId;
    private final Long productId;
    private final long quantity;

    public static MenuProductResponse of(final Long menuId, final MenuProduct menuProduct) {
        return new MenuProductResponse(menuProduct.getSeq(), menuId, menuProduct.getProductId(),
            menuProduct.getQuantity());
    }
}
