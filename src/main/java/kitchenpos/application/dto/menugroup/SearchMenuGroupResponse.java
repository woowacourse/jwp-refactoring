package kitchenpos.application.dto.menugroup;

import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.domain.MenuGroup;

public class SearchMenuGroupResponse {

    @JsonProperty("id")
    private final Long id;
    @JsonProperty("name")
    private final String name;

    private SearchMenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static SearchMenuGroupResponse from(MenuGroup menuGroup) {
        return new SearchMenuGroupResponse(menuGroup.id(), menuGroup.name());
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }
}
