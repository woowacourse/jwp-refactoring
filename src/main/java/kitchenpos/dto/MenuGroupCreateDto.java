package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;

public class MenuGroupCreateDto {

    private final String name;

    public MenuGroupCreateDto(final String name) {
        this.name = name;
    }

    public MenuGroup toDomain() {
        return new MenuGroup(name);
    }

    public String getName() {
        return name;
    }
}
