package kitchenpos.menugroup.application.dto;

import kitchenpos.menugroup.domain.MenuGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MenuGroupResponse {
    private final Long id;
    private final String name;

    public static MenuGroupResponse from(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName());
    }
}
