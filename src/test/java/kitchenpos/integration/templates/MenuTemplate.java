package kitchenpos.integration.templates;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.factory.MenuFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class MenuTemplate {

    public static final String MENU_URL = "/api/menus";

    private final IntegrationTemplate integrationTemplate;

    public MenuTemplate(IntegrationTemplate integrationTemplate) {
        this.integrationTemplate = integrationTemplate;
    }

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

        return integrationTemplate.post(
            MENU_URL,
            menu,
            Menu.class
        );
    }

    public ResponseEntity<Menu[]> list() {
        return integrationTemplate.get(
            MENU_URL,
            Menu[].class
        );
    }
}
