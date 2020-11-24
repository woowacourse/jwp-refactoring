package kitchenpos.menu.dto;

import javax.validation.constraints.NotBlank;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupCreateRequest {

    @NotBlank
    private String name;

    public MenuGroupCreateRequest() {
    }

    public MenuGroupCreateRequest(@NotBlank String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }
}
