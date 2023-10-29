package kitchenpos.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sun.istack.NotNull;

public class CreateMenuGroupRequest {

    @NotNull
    private String name;

    @JsonCreator
    public CreateMenuGroupRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
