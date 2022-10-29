package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupDto {

    private Long id;
    private String name;

    public MenuGroupDto() {
    }

    public MenuGroupDto(String name) {
        this.name = name;
    }

    public MenuGroupDto(MenuGroup menuGroup) {
        this.id = menuGroup.getId();
        this.name = menuGroup.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
