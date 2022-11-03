package kitchenpos.menu.presentation.dto;

import kitchenpos.menu.application.dto.MenuGroupRequestDto;

public class MenuGroupRequest {

    private String name;

    public MenuGroupRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public MenuGroupRequestDto toServiceDto(){
        return new MenuGroupRequestDto(name);
    }
}
