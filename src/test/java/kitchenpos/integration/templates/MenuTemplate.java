package kitchenpos.integration.templates;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.factory.MenuProductFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class MenuTemplate {

    public static final String MENU_URL = "/api/menus";

    private final IntegrationTemplate integrationTemplate;

    public MenuTemplate(IntegrationTemplate integrationTemplate) {
        this.integrationTemplate = integrationTemplate;
    }

    public ResponseEntity<MenuResponse> create(String name,
                                       BigDecimal price,
                                       Long menuGroupId,
                                       List<MenuProduct> menuProducts) {
        MenuRequest menuRequest = new MenuRequest(
            null,
            name,
            price,
            menuGroupId,
            MenuProductFactory.dtoList(menuProducts)
        );

        return integrationTemplate.post(
            MENU_URL,
            menuRequest,
            MenuResponse.class
        );
    }

    public ResponseEntity<MenuResponse[]> list() {
        return integrationTemplate.get(
            MENU_URL,
            MenuResponse[].class
        );
    }
}
