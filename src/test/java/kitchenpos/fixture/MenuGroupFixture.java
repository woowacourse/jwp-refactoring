package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

@SuppressWarnings("NonAsciiCharacters")
public class MenuGroupFixture {

    public static MenuGroup 추천메뉴() {
        return new MenuGroup(1L, "추천메뉴");
    }
}
