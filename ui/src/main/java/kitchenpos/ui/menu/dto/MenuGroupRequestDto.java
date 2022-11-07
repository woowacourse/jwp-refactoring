package kitchenpos.ui.menu.dto;

import kitchenpos.application.menu.dto.request.CreateMenuGroupDto;

public class MenuGroupRequestDto {

    private String name;

    public MenuGroupRequestDto() {
    }

    public CreateMenuGroupDto toCreateMenuGroupDto() {
        return new CreateMenuGroupDto(name);
    }

    public String getName() {
        return name;
    }
}
