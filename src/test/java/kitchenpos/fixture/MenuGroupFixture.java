package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

@SuppressWarnings("NonAsciiCharacters")
public class MenuGroupFixture {

    public static MenuGroup 메뉴그룹_신메뉴() {
        final var menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("신메뉴");
        return menuGroup;
    }
}
