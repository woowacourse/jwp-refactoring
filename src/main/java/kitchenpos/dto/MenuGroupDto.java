package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupDto {

    private Long id;
    private String name;

    public MenuGroupDto() {
    }

    public MenuGroupDto(final MenuGroup menuGroup) {
        this.id = menuGroup.getId();
        this.name = menuGroup.getName();
    }

    public MenuGroup toEntity() {
        return new MenuGroup(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
