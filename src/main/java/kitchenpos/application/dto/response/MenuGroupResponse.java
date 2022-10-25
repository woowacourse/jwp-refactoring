package kitchenpos.application.dto.response;

import kitchenpos.domain.MenuGroup;

public record MenuGroupResponse(Long id, String name) {

    public static MenuGroupResponse from(final MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }
}
