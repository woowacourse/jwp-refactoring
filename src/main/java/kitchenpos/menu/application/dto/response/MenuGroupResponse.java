package kitchenpos.menu.application.dto.response;

import kitchenpos.menu.domain.MenuGroup;

public record MenuGroupResponse(Long id, String name) {

    public static MenuGroupResponse from(final MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }
}
