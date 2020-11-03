package kitchenpos.dto;

import javax.validation.constraints.NotBlank;

import kitchenpos.domain.MenuGroup;

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
