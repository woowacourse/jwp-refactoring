package kitchenpos.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import kitchenpos.domain.MenuGroup;

public class MenuGroupCreateRequest {
    private final String name;

    @JsonCreator
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
