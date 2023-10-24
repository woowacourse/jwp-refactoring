package kitchenpos.menu.dto.request;

import javax.validation.constraints.NotNull;

public class MenuGroupCreationRequest {

    @NotNull
    private final String name;

    private MenuGroupCreationRequest() {
        this(null);
    }

    public MenuGroupCreationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
