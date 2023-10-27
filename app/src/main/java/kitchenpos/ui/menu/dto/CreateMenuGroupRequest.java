package kitchenpos.ui.menu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.application.menu.dto.CreateMenuGroupCommand;

public class CreateMenuGroupRequest {

    @JsonProperty("name")
    private String name;

    public CreateMenuGroupRequest() {
    }

    public CreateMenuGroupRequest(String name) {
        this.name = name;
    }

    public CreateMenuGroupCommand toCommand() {
        return new CreateMenuGroupCommand(name);
    }

    public String name() {
        return name;
    }
}
