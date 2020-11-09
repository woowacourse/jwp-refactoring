package kitchenpos.dto.menugroup;

import javax.validation.constraints.NotBlank;

import kitchenpos.domain.menugroup.MenuGroup;

public class MenuGroupRequest {

    @NotBlank
    private String name;

    protected MenuGroupRequest() {
    }

    public MenuGroupRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(this.name);
    }
}
