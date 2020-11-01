package kitchenpos.dto;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;

import java.util.List;

public class CreateMenuRequest {
    private String name;
    private Long price;
    private MenuGroup menuGroup;
    private List<MenuProduct> menuProducts;

    public CreateMenuRequest(String name, Long price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public Menu getMenu() {
        return new Menu(name, price, menuGroup);
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
