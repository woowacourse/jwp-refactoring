package kitchenpos.fixture;

import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.menuGroup.application.request.MenuGroupRequest;

@SuppressWarnings("NonAsciiCharacters")
public enum MenuGroupFixture {

    두마리메뉴("두마리메뉴"),
    ;

    private final String name;

    MenuGroupFixture(String name) {
        this.name = name;
    }

    public MenuGroupRequest toRequest() {
        return new MenuGroupRequest(name);
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(name);
    }
}
