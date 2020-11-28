package kitchenpos.menugroup.application.dto;

import kitchenpos.menugroup.model.MenuGroup;

public class MenuGroupCreateRequestDto {
    private String name;

    private MenuGroupCreateRequestDto() {
    }

    public MenuGroupCreateRequestDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(null, name);
    }
}
