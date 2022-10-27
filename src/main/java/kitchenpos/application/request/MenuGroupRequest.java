package kitchenpos.application.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class MenuGroupRequest {

    private String name;

    @JsonCreator
    public MenuGroupRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
