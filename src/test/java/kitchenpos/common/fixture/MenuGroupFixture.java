package kitchenpos.common.fixture;

import kitchenpos.domain.MenuGroup;

@SuppressWarnings("NonAsciiCharacters")
public class MenuGroupFixture {

    public static MenuGroup 새_메뉴_그룹(Long menuGroupId) {
        return new MenuGroup(menuGroupId, "menuGroupName");
    }

    public static MenuGroup 새_메뉴_그룹() {
        return new MenuGroup("menuGroupName");
    }
}
