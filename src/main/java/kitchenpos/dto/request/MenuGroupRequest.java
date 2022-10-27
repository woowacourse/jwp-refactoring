package kitchenpos.dto.request;

import com.sun.istack.NotNull;

public class MenuGroupRequest {

    @NotNull
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
