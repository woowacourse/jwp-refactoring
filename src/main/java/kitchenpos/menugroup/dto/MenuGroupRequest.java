package kitchenpos.menugroup.dto;

import java.beans.ConstructorProperties;

import javax.validation.constraints.NotEmpty;

import kitchenpos.menugroup.domain.MenuGroup;

public class MenuGroupRequest {
    @NotEmpty(message = "이름을 입력해주세요.")
    private final String name;

    @ConstructorProperties({"name"})
    public MenuGroupRequest(final String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(name);
    }

    public String getName() {
        return name;
    }
}
