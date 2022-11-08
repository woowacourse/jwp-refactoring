package kitchenpos.application.dto.request;

import com.sun.istack.NotNull;

public class MenuGroupRequest {
    @NotNull
    private String name;

    private MenuGroupRequest() {
    }

    public MenuGroupRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
