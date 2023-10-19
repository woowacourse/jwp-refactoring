package kitchenpos.fixture;

import kitchenpos.menugroup.domain.MenuGroup;

@SuppressWarnings("NonAsciiCharacters")
public class MenuGroupFixture {

    public static MenuGroup 메뉴_그룹_생성() {
        return new MenuGroup(null, "메뉴그룹");
    }

    public static MenuGroup 메뉴_그룹_생성(final String name) {
        return new MenuGroup(null, name);
    }
}
