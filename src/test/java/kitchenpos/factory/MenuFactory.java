package kitchenpos.factory;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.dto.MenuRequest;

public class MenuFactory {

    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private MenuProducts menuProducts;

    private MenuFactory() {
}

    private MenuFactory(Long id,
                        String name,
                        BigDecimal price,
                        Long menuGroupId,
                        MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuFactory builder() {
        return new MenuFactory();
    }

    public static MenuFactory copy(Menu menu) {
        return new MenuFactory(
            menu.getId(),
            menu.getName(),
            menu.getPrice(),
            menu.getMenuGroupId(),
            menu.getMenuProducts()
        );
    }

    public static MenuRequest dto(Menu menu) {
        return new MenuRequest(
            menu.getId(),
            menu.getName(),
            menu.getPrice(),
            menu.getMenuGroupId(),
            MenuProductFactory.dtoList(menu.getMenuProducts())
        );
    }

    public MenuFactory id(Long id) {
        this.id = id;
        return this;
    }

    public MenuFactory name(String name) {
        this.name = name;
        return this;
    }

    public MenuFactory price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public MenuFactory menuGroupId(Long menuGroupId) {
        this.menuGroupId = menuGroupId;
        return this;
    }

    public MenuFactory menuProducts(MenuProduct... menuProducts) {
        this.menuProducts = new MenuProducts(menuProducts);
        return this;
    }

    public Menu build() {
        return new Menu(id, name, price, menuGroupId, menuProducts);
    }
}
