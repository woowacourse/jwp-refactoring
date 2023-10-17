package kitchenpos.application.dto.menu;

import static kitchenpos.exception.MenuExceptionType.MENU_PRODUCT_COMMANDS_CAN_NOT_NULL;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import kitchenpos.application.dto.menuproduct.MenuProductCommand;
import kitchenpos.exception.MenuException;

public class CreateMenuCommand {

    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductCommand> menuProductCommands;

    public CreateMenuCommand(
            String name,
            BigDecimal price,
            Long menuGroupId,
            List<MenuProductCommand> menuProductCommands
    ) {
        if (Objects.isNull(menuProductCommands)) {
            throw new MenuException(MENU_PRODUCT_COMMANDS_CAN_NOT_NULL);
        }
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProductCommands = menuProductCommands;
    }

    public String name() {
        return name;
    }

    public BigDecimal price() {
        return price;
    }

    public Long menuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductCommand> menuProductCommands() {
        return menuProductCommands;
    }
}
