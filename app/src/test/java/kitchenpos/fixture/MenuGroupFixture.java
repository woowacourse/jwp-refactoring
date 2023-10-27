package kitchenpos.fixture;


import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup from(String name) {
        return new MenuGroup(name);
    }
}
