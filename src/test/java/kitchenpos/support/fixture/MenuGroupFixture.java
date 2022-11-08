package kitchenpos.support.fixture;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup createWithId(final Long id) {
        return new MenuGroup(id, "name");
    }

    public static MenuGroup createDefaultWithoutId() {
        return createWithId(null);
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
