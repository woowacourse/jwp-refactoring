package kitchenpos.application.dto;

import kitchenpos.domain.MenuProduct;
import kitchenpos.ui.dto.request.MenuProductRequest;

public class MenuProductCreationDto {

    private final Long productId;
    private final int quantity;

    private MenuProductCreationDto(final Long productId, final int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProductCreationDto from(final MenuProductRequest menuProductRequest) {
        return new MenuProductCreationDto(menuProductRequest.getProductId(), menuProductRequest.getQuantity());
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
