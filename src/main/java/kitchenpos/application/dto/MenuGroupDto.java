package kitchenpos.application.dto;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;

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
