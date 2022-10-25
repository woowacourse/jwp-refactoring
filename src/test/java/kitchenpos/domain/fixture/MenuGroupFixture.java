package kitchenpos.domain.fixture;

import kitchenpos.domain.MenuGroup;

@SuppressWarnings("NonAsciiCharacters")
public class MenuGroupFixture {

    private Long id;
    private String name;

    private MenuGroupFixture() {
    }

    public static MenuGroupFixture 치킨_세트() {
        final MenuGroupFixture menuGroupFixture = new MenuGroupFixture();
        menuGroupFixture.name = "치킨 세트";
        return menuGroupFixture;
    }

    public MenuGroup build() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }
}
