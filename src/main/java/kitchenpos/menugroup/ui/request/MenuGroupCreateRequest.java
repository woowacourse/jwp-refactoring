package kitchenpos.menugroup.ui.request;

import javax.validation.constraints.NotBlank;

public class MenuGroupCreateRequest {

    @NotBlank
    private final String name;

    public MenuGroupCreateRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
