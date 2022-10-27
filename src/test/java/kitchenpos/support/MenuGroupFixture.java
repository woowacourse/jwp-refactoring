package kitchenpos.support;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup 메뉴_그룹 = 메뉴_그룹_생성("메뉴_그룹1");

    public static MenuGroup 메뉴_그룹_생성(final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
}
