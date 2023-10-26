package kitchenpos.application.menu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.domain.menu.MenuGroup;

public class MenuGroupResponse {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;

    private MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroupResponse from(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.id(), menuGroup.name());
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }
}
