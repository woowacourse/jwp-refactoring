package kitchenpos.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MenuGroupCreateRequest {

    private final String name;

    @JsonCreator
    public MenuGroupCreateRequest(@JsonProperty("name") final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
