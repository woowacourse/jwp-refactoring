package kitchenpos.fixture;

import kitchenpos.domain.menugroup.MenuGroup;

@SuppressWarnings("NonAsciiCharacters")
public class MenuGroupFixture {

    public static MenuGroup 추천메뉴() {
        return new MenuGroup(1L, "추천메뉴");
    }
}
