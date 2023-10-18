package kitchenpos.application.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupDto {

    private final Long id;
    private final String name;

    public MenuGroupDto(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupDto from(final MenuGroup menuGroup) {
        return new MenuGroupDto(menuGroup.getId(), menuGroup.getName());
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
