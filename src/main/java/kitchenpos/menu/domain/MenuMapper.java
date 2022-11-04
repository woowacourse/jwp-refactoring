package kitchenpos.menu.domain;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper {
    public Menu mapMenu(Menu save, List<MenuProduct> menuProducts) {
        Menu menu = new Menu(save.getId(),
                save.getName(),
                save.getPriceValue(),
                save.getMenuGroupId(),
                new MenuProducts(menuProducts));
        menu.placeMenuId();
        return menu;
    }
}
