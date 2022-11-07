package kitchenpos.ui.dto;

import kitchenpos.application.dto.request.CreateMenuGroupDto;

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
