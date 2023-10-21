package kitchenpos.dto.request;

import javax.validation.constraints.NotNull;

public class MenuGroupCreationRequest {

    @NotNull
    private final String name;

    public MenuGroupCreationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
