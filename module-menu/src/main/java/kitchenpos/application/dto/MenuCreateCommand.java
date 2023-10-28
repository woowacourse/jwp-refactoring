package kitchenpos.application.dto;

import java.math.BigDecimal;
import java.util.List;

public class MenuCreateCommand {

    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<MenuProductCreateCommand> menuProducts;

    public MenuCreateCommand(
            String name,
            BigDecimal price,
            Long menuGroupId,
            List<MenuProductCreateCommand> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

    public List<MenuProductCreateCommand> menuProducts() {
        return menuProducts;
    }
}
