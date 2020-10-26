package kitchenpos.factory;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

@Component
public class MenuFactory {
    public Menu create(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        return menu;
    }

    public Menu create(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return create(null, name, price, menuGroupId, menuProducts);
    }
}
