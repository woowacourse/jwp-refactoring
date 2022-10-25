package kitchenpos.application.dto.request;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProducts;

public record MenuCommand(String name,
                          BigDecimal price,
                          Long menuGroupId,
                          List<MenuProductCommand> menuProductCommands) {

    public Menu toEntity() {
        return new Menu(name, price, menuGroupId);
    }

    public MenuProducts toMenuProducts() {
        return new MenuProducts(menuProductCommands.stream()
                .map(MenuProductCommand::toEntity)
                .toList());
    }
}
