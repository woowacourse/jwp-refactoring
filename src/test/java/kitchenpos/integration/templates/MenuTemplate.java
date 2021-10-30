package kitchenpos.integration.templates;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.factory.MenuFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class MenuTemplate extends IntegrationTemplate {

    public static final String MENU_URL = "/api/menus";

    public ResponseEntity<Menu> create(String name,
                                       BigDecimal price,
                                       Long menuGroupId,
                                       List<MenuProduct> menuProducts) {
        Menu menu = MenuFactory.builder()
                .name(name)
                .price(price)
                .menuGroupId(menuGroupId)
                .menuProducts(menuProducts)
                .build();

        return post(
                MENU_URL,
                menu,
                Menu.class
        );
    }

    public ResponseEntity<Menu[]> list() {
        return get(
                MENU_URL,
                Menu[].class
        );
    }
}
