package kitchenpos.application.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import kitchenpos.domain.MenuGroup;

public class MenuGroupResponse {

    private Long id;
    private String name;

    @JsonCreator
    public MenuGroupResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroupResponse(final MenuGroup menuGroup) {
        this(menuGroup.getId(), menuGroup.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
