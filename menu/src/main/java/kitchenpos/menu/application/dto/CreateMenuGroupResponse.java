package kitchenpos.menu.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.menu.domain.MenuGroup;

public class CreateMenuGroupResponse {

    @JsonProperty("id")
    private final Long id;
    @JsonProperty("name")
    private final String name;

    private CreateMenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static CreateMenuGroupResponse from(MenuGroup menuGroup) {
        return new CreateMenuGroupResponse(menuGroup.id(), menuGroup.name());
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }
}
