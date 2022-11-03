package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.MenuGroupRequest;

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
        return new MenuGroup(null, name);
    }

    public MenuGroupRequest toRequest() {
        return new MenuGroupRequest(name);
    }
}
