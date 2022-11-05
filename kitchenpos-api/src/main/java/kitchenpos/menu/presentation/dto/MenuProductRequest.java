package kitchenpos.menu.presentation.dto;

import kitchenpos.menu.application.dto.request.MenuProductCommand;

public record MenuProductRequest(Long productId, long quantity) {

    public MenuProductCommand toCommand() {
        return new MenuProductCommand(productId, quantity);
    }
}
