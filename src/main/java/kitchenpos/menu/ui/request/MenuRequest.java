package kitchenpos.menu.ui.request;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.request.MenuCommand;
import kitchenpos.menu.application.dto.request.MenuProductCommand;

public class MenuRequest {

    private String name;
    private BigDecimal price;
    private long menuGroupId;
    private List<MenuProductRequest> menuProducts;

    private MenuRequest() {
    }

    public MenuRequest(String name, BigDecimal price, long menuGroupId, List<MenuProductRequest> menuProducts) {
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

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public MenuCommand toCommand() {
        return new MenuCommand(name, price, menuGroupId, toCommands(menuProducts));
    }

    private List<MenuProductCommand> toCommands(List<MenuProductRequest> menuProducts) {
        return menuProducts.stream()
                .map(it -> new MenuProductCommand(it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());
    }
}
