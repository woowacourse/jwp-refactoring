package kitchenpos.ui.dto;

import kitchenpos.application.dto.request.MenuProductCommand;

public record MenuProductRequest(Long productId, long quantity) {

    public MenuProductCommand toCommand() {
        return new MenuProductCommand(productId, quantity);
    }
}
