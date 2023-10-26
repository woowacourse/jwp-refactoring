package kitchenpos.fixture;

import kitchenpos.menugroup.domain.MenuGroup;

@SuppressWarnings("NonAsciiCharacters")
public class MenuGroupFixture {

    public static MenuGroup 추천_메뉴_그룹() {
        return MenuGroup.from("추천 메뉴");
    }

    public static MenuGroup 후추와_함께하는_메뉴() {
        return MenuGroup.from("후추와 함께하는 메뉴");
    }

    public static MenuGroup 후추는_천재_메뉴() {
        return MenuGroup.from("후추는 천재 메뉴");
    }

}
