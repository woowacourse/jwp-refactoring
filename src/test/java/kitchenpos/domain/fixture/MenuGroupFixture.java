package kitchenpos.domain.fixture;

import kitchenpos.domain.MenuGroup;

@SuppressWarnings("NonAsciiCharacters")
public class MenuGroupFixture {

    private Long id;
    private String name;

    private MenuGroupFixture() {
    }

    public static MenuGroup 치킨_세트() {
        return MenuGroupFixture.메뉴_그룹()
            .이름("치킨 세트")
            .build();
    }

    private static MenuGroupFixture 메뉴_그룹() {
        return new MenuGroupFixture();
    }

    private MenuGroupFixture 이름(final String name) {
        this.name = name;
        return this;
    }

    private MenuGroup build() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }
}
