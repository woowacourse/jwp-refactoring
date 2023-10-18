package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

@SuppressWarnings("NonAsciiCharacters")
public class MenuGroupFixture {

    public static MenuGroup 추천_메뉴_그룹() {
        return new MenuGroup("추천 메뉴");
    }

    public static MenuGroup 떠오르는_메뉴_그룹() {
        return new MenuGroup("떠오르는 메뉴");
    }

    public static MenuGroup 싼_메뉴_그룹() {
        return new MenuGroup("싼 메뉴");
    }

}
