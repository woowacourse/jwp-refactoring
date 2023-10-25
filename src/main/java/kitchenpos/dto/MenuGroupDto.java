package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupDto {

    private final Long id;
    private final String name;

    public MenuGroupDto(MenuGroup menuGroup) {
        this(menuGroup.getId(), menuGroup.getName());
    }

    public MenuGroupDto(String name) {
        this(null, name);
    }

    public MenuGroupDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
