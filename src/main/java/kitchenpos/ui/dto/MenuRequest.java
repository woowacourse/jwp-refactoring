package kitchenpos.ui.dto;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.request.MenuCommand;
import kitchenpos.application.dto.request.MenuProductCommand;

public record MenuRequest(String name,
                          BigDecimal price,
                          Long menuGroupId,
                          List<MenuProductRequest> menuProductRequests) {

    public MenuCommand toCommand() {
        return new MenuCommand(name, price, menuGroupId, toMenuProductCommands());
    }

    public List<MenuProductCommand> toMenuProductCommands() {
        return menuProductRequests.stream()
                .map(MenuProductRequest::toCommand)
                .toList();
    }
}
