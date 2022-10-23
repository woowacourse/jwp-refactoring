package kitchenpos.support;

import kitchenpos.domain.MenuGroup;

public enum MenuGroupFixture {

    MENU_GROUP_1("메뉴그룹1");

    private String name;

    MenuGroupFixture(final String name) {
        this.name = name;
    }

    public MenuGroup 생성() {
        return new MenuGroup(this.name);
    }
}
