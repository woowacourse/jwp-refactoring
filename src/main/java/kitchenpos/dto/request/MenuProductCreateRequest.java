package kitchenpos.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import kitchenpos.domain.MenuProduct;

public class MenuProductCreateRequest {

    @NotNull
    private final Long productId;

    @Positive
    private final int quantity;

    public MenuProductCreateRequest(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct toEntity(Long menuId) {
        return new MenuProduct(menuId, this.productId, this.quantity);
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
