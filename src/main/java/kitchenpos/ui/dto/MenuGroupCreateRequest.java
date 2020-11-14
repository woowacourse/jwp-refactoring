package kitchenpos.ui.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import kitchenpos.domain.MenuGroup;

public class MenuGroupCreateRequest {
    private String name;

    @JsonCreator
    public MenuGroupCreateRequest(String name) {
        this.name = name;
    }

    public MenuGroup toEntity() {
        return new MenuGroup(null, name);
    }

    public String getName() {
        return name;
    }
}
