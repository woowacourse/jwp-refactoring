package kitchenpos.application.dto;

import javax.validation.constraints.NotBlank;

import kitchenpos.domain.entity.MenuGroup;

public class MenuGroupRequest {
    @NotBlank
    private String name;

    private MenuGroupRequest() {
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(null, name);
    }

    public String getName() {
        return name;
    }
}
