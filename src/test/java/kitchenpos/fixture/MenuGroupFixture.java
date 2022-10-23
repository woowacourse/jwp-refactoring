package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

@SuppressWarnings("NonAsciiCharacters")
public enum MenuGroupFixture {
    분식("분식"),
    양식("양식")
    ;

    private final String name;

    MenuGroupFixture(final String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
}
