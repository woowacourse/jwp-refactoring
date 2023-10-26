package kitchenpos.menu.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class MenuGroupCreateRequest {

    private final String name;

    @JsonCreator
    public MenuGroupCreateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
