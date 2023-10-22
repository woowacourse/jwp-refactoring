package kitchenpos.ui.dto;

import kitchenpos.domain.MenuGroup;
import lombok.Data;

@Data
public class MenuGroupResponse {
    private final Long id;
    private final String name;

    public static MenuGroupResponse from(final MenuGroup menuGroup) {
        return new MenuGroupResponse(
                menuGroup.getId(),
                menuGroup.getName()
        );
    }
}
