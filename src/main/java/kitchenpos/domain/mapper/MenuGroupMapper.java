package kitchenpos.domain.mapper;

import kitchenpos.application.dto.request.CreateMenuGroupRequest;
import kitchenpos.domain.MenuGroup;

public class MenuGroupMapper {
    private MenuGroupMapper() {
    }

    public static MenuGroup toMenuGroup(final CreateMenuGroupRequest menuGroup) {
        return MenuGroup.builder()
                .name(menuGroup.getName())
                .build();
    }
}
