package kitchenpos.ui.dto.response;

import kitchenpos.application.dto.MenuProductDto;
import kitchenpos.domain.MenuProduct;

public class MenuProductResponse {

    private final Long id;
    private final Long menuId;
    private final Long productId;
    private final long quantity;

    private MenuProductResponse(final Long id, final Long menuId, final Long productId, final long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductResponse from(final MenuProductDto menuProductDto) {
        return new MenuProductResponse(menuProductDto.getId(),
                menuProductDto.getMenuId(),
                menuProductDto.getProductId(),
                menuProductDto.getQuantity());
    }


    public Long getId() {
        return id;
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
