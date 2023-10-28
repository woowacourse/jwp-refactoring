package kitchenpos.menugroup.request;

import javax.validation.constraints.NotBlank;

public class MenuGroupCreateRequest {

    @NotBlank
    private String name;

    private MenuGroupCreateRequest() {
    }

    public MenuGroupCreateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
