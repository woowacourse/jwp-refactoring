package kitchenpos.support.fixture;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup createSaleMenuGroup() {
        return new MenuGroup("할인메뉴");
    }

    public static MenuGroup createSuggestionMenuGroup() {
        return new MenuGroup("추천메뉴");
    }
}
