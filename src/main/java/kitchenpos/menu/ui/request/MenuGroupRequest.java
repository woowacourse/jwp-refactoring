package kitchenpos.menu.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class MenuGroupRequest {

    private final String name;

    @JsonCreator
    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
