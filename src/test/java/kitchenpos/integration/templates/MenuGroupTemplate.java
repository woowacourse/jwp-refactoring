package kitchenpos.integration.templates;

import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
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
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(null, name);

        return integrationTemplate.post(
            MENU_GROUP_URL,
            menuGroupRequest,
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
