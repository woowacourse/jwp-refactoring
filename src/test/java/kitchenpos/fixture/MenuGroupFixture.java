package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

@SuppressWarnings("NonAsciiCharacters")
public final class MenuGroupFixture {

    public static MenuGroup 메뉴_그룹_생성() {
        final MenuGroup menuGroup = new MenuGroup();

        menuGroup.setName("메뉴 그룹");

        return menuGroup;
    }

    public static MenuGroup 메뉴_그룹_생성(final String name) {
        final MenuGroup menuGroup = new MenuGroup();

        menuGroup.setName(name);

        return menuGroup;
    }

    private MenuGroupFixture() {
    }
}
