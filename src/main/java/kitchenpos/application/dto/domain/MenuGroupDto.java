package kitchenpos.application.dto.domain;

import kitchenpos.domain.menugroup.MenuGroup;

public class MenuGroupDto {
    private Long id;
    private String name;

    public static MenuGroupDto from(final MenuGroup menuGroup) {
        return new MenuGroupDto(menuGroup.getId(), menuGroup.getName());
    }

    public MenuGroupDto(final Long id, final String name) {
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
