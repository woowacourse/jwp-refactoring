package kitchenpos.dto;

import com.sun.istack.NotNull;

public class MenuGroupRequest {
    @NotNull
    private String name;

    public MenuGroupRequest() {
    }

    public MenuGroupRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
