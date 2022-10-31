package kitchenpos.menu.presentation.dto;

import kitchenpos.menu.application.dto.MenuProductSaveRequest;

public class MenuProductCreateRequest {

    private Long productId;
    private Integer quantity;

    public MenuProductCreateRequest() {
    }

    public MenuProductCreateRequest(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProductSaveRequest toResponse() {
        return new MenuProductSaveRequest(productId, quantity);
    }
}
