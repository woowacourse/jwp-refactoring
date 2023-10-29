package kitchenpos.menu.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MenuGroupCreateRequest {

    final String name;

    public MenuGroupCreateRequest(@JsonProperty("name") final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
