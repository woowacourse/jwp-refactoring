package kitchenpos.ui.dto.response;

import kitchenpos.domain.MenuGroup;

public class CreateMenuGroupResponse {

    private final Long id;
    private final String name;

    public CreateMenuGroupResponse(final MenuGroup menuGroup) {
        this.id = menuGroup.getId();
        this.name = menuGroup.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
