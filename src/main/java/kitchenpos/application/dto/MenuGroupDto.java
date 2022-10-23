package kitchenpos.application.dto;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import lombok.Getter;

@Getter
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
}
