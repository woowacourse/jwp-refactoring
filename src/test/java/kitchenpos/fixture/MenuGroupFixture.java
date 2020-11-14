package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.MenuGroupCreateRequest;

public class MenuGroupFixture {
    public static MenuGroupCreateRequest createMenuGroupRequest(String name) {
        return new MenuGroupCreateRequest(name);
    }

    public static MenuGroup createMenuGroup(Long id, String name) {
        return new MenuGroup(id, name);
    }
}
