package kitchenpos.ui.dto.response;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;

public class MenuGroupResponse {

    private final Long id;
    private final String name;

    public MenuGroupResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroupResponse(final MenuGroup menuGroup) {
        this(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
