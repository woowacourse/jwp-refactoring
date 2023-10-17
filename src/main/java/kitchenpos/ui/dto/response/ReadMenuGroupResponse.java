package kitchenpos.ui.dto.response;

import kitchenpos.domain.MenuGroup;

public class ReadMenuGroupResponse {

    private final Long id;
    private final String name;

    public ReadMenuGroupResponse(final MenuGroup menuGroup) {
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
