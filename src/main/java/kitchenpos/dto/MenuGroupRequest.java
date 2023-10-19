package kitchenpos.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class MenuGroupRequest {

    private String name;

    @JsonCreator
    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
