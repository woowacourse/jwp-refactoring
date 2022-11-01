package kitchenpos.application.dto.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

public class MenuCommand {

    private final String name;
    private final BigDecimal price;
    private final long menuGroupId;
    private final List<MenuProductCommand> menuProducts;

    public MenuCommand(String name, BigDecimal price, long menuGroupId, List<MenuProductCommand> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductCommand> getMenuProducts() {
        return menuProducts;
    }

}
