package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup createDefaultWithoutId() {
        return new MenuGroup(null, "name");
    }

    public static MenuGroup createDefaultWithNotSavedId() {
        return new MenuGroup(-1L, "name");
    }

    public static MenuGroup requestCreate(final int port) {
        return RestTemplateFixture.create()
                .postForEntity("http://localhost:" + port + "/api/menu-groups",
                        createDefaultWithoutId(), MenuGroup.class)
                .getBody();
    }
}
