package kitchenpos.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class MenuGroupCreationRequest {

    private final String name;

    @JsonCreator
    public MenuGroupCreationRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
