package kitchenpos.menu.presentation.dto;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.application.dto.request.MenuCommand;
import kitchenpos.menu.application.dto.request.MenuProductCommand;

public record MenuRequest(String name,
                          BigDecimal price,
                          Long menuGroupId,
                          List<MenuProductRequest> menuProducts) {

    public MenuCommand toCommand() {
        return new MenuCommand(name, price, menuGroupId, toMenuProductCommands());
    }

    public List<MenuProductCommand> toMenuProductCommands() {
        return menuProducts.stream()
                .map(MenuProductRequest::toCommand)
                .toList();
    }
}
