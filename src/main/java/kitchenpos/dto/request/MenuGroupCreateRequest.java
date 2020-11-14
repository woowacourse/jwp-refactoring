package kitchenpos.dto.request;

import java.beans.ConstructorProperties;

import kitchenpos.domain.MenuGroup;

public class MenuGroupCreateRequest {
    private final String name;

    @ConstructorProperties({"name"})
    public MenuGroupCreateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public MenuGroup toEntity() {
        return MenuGroup.of(this.name);
    }
}
