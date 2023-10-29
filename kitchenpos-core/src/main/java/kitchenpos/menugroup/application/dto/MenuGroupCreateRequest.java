package kitchenpos.menugroup.application.dto;

import javax.validation.constraints.NotNull;

public class MenuGroupCreateRequest {

    @NotNull
    private String name;

    public MenuGroupCreateRequest() {
    }

    public MenuGroupCreateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
