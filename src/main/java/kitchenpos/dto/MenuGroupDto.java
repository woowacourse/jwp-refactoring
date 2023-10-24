package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupDto {

    private final Long id;
    private final String name;

    private MenuGroupDto(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupDto toDto(final MenuGroup menugroup) {
        return new MenuGroupDto(menugroup.getId(), menugroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
