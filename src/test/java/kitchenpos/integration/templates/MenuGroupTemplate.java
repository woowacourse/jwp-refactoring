package kitchenpos.integration.templates;

import kitchenpos.domain.MenuGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class MenuGroupTemplate {

    public static final String MENU_GROUP_URL = "/api/menu-groups";

    private final IntegrationTemplate integrationTemplate;

    public MenuGroupTemplate(IntegrationTemplate integrationTemplate) {
        this.integrationTemplate = integrationTemplate;
    }

    public ResponseEntity<MenuGroup> create(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return integrationTemplate.post(
            MENU_GROUP_URL,
            menuGroup,
            MenuGroup.class
        );
    }

    public ResponseEntity<MenuGroup[]> list() {
        return integrationTemplate.get(
            MENU_GROUP_URL,
            MenuGroup[].class
        );
    }

}
