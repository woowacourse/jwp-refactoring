package kitchenpos.ui.dto;

import javax.validation.constraints.NotBlank;

public class MenuGroupRequest {

    @NotBlank
    private String name;

    public MenuGroupRequest() {
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
