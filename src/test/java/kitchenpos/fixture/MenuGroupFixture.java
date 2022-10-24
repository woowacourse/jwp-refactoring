package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup createDefaultWithoutId() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("name");
        return menuGroup;
    }

    public static MenuGroup requestCreate(final int port) {
        return RestTemplateFixture.create()
                .postForEntity("http://localhost:" + port + "/api/menu-groups",
                        createDefaultWithoutId(), MenuGroup.class)
                .getBody();
    }
}
