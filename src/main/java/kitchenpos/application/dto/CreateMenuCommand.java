package kitchenpos.application.dto;

import java.math.BigDecimal;
import java.util.List;

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
