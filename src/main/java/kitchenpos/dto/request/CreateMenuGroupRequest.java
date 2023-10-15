package kitchenpos.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public class CreateMenuGroupRequest {

    private String name;

    @JsonCreator
    public CreateMenuGroupRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
