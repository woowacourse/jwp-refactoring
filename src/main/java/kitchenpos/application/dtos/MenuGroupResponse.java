package kitchenpos.application.dtos;

import kitchenpos.domain.MenuGroup;
import lombok.Getter;

@Getter
public class MenuGroupResponse {
    private final Long id;
    private final String name;

    public MenuGroupResponse(MenuGroup menuGroup) {
        this.id = menuGroup.getId();
        this.name = menuGroup.getName();
    }
}
