package kitchenpos.mapper;

import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupDto;

public class MenuGroupMapper {

    private MenuGroupMapper() {
    }

    public static MenuGroupDto toDto(MenuGroup menuGroup) {
        MenuGroupDto menuGroupDto = new MenuGroupDto();
        menuGroupDto.setId(menuGroup.getId());
        menuGroupDto.setName(menuGroup.getName());
        return menuGroupDto;
    }

    public static MenuGroup toEntity(MenuGroupDto menuGroupDto) {
        return new MenuGroup.Builder()
            .id(menuGroupDto.getId())
            .name(menuGroupDto.getName())
            .build();
    }
}
