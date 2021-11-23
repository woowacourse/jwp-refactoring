package kitchenpos.application.dto;

import kitchenpos.application.dto.response.MenuGroupResponseDto;
import kitchenpos.domain.MenuGroup;

public class MenuGroupDtoAssembler {

    private MenuGroupDtoAssembler() {
    }

    public static MenuGroupResponseDto menuGroupResponseDto(MenuGroup menuGroup) {
        return new MenuGroupResponseDto(menuGroup.getId(), menuGroup.getName());
    }
}
