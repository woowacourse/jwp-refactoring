package kitchenpos.menu.ui.dto;

import kitchenpos.common.vo.Price;
import kitchenpos.menu.domain.MenuProduct;

public class MenuProductDto {

    private final Long productId;
    private final Long quantity;

    public MenuProductDto(final Long productId, final Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toEntity(final Price price) {
        return new MenuProduct(productId, price, quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
