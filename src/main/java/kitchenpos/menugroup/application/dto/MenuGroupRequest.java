package kitchenpos.menugroup.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MenuGroupRequest {

    private String name;

    @JsonCreator
    public MenuGroupRequest(@JsonProperty("name") final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
