package kitchenpos.domain.menugroup;

import kitchenpos.application.dto.request.CreateMenuGroupRequest;

public class MenuGroupMapper {
    private MenuGroupMapper() {
    }

    public static MenuGroup toMenuGroup(final CreateMenuGroupRequest menuGroup) {
        return MenuGroup.builder()
                .name(menuGroup.getName())
                .build();
    }
}
