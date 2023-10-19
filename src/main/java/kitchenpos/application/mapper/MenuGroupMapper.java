package kitchenpos.application.mapper;

import kitchenpos.application.dto.request.MenuGroupCreateRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.domain.MenuGroup;

public class MenuGroupMapper {

    private MenuGroupMapper() {
    }

    public static MenuGroup mapToMenuGroup(final MenuGroupCreateRequest menuGroupCreateRequest) {
        return new MenuGroup(menuGroupCreateRequest.getName());
    }

    public static MenuGroupResponse mapToResponse(final MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }
}
