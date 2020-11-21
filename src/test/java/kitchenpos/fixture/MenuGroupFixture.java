package kitchenpos.fixture;

import kitchenpos.application.dto.MenuGroupCreateRequest;
import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {
    public static MenuGroup createMenuGroup(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public static MenuGroupCreateRequest createMenuGroupRequest(String name) {
        return new MenuGroupCreateRequest(name);
    }
}
