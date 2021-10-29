package kitchenpos.integration.templates;

import kitchenpos.domain.MenuGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class MenuGroupTemplate extends IntegrationTemplate {

    public static final String MENU_GROUP_URL = "/api/menu-groups";

    public ResponseEntity<MenuGroup> create(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return post(
                MENU_GROUP_URL,
                menuGroup,
                MenuGroup.class
        );
    }

    public ResponseEntity<MenuGroup[]> list() {
        return get(
                MENU_GROUP_URL,
                MenuGroup[].class
        );
    }

}
