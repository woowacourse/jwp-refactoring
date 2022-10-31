package kitchenpos.application.dto.response;

import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupDto {

    private final Long id;
    private final String name;

    private MenuGroupDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupDto of(MenuGroup menuGroup) {
        return new MenuGroupDto(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
