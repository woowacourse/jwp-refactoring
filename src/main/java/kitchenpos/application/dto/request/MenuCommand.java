package kitchenpos.application.dto.request;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.MenuProduct;

public record MenuCommand(String name,
                          BigDecimal price,
                          Long menuGroupId,
                          List<MenuProductCommand> menuProductCommands) {

    public List<MenuProduct> toRawMenuProducts() {
        return menuProductCommands.stream()
                .map(MenuProductCommand::toEntity)
                .toList();
    }
}
