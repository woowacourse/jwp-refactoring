package kitchenpos.ui.dto;

import kitchenpos.domain.MenuGroup;
import lombok.Data;

@Data
public class MenuGroupRequest {
    private final Long id;
    private final String name;

    public MenuGroup convert() {
        return new MenuGroup(
                id,
                name
        );
    }
}
