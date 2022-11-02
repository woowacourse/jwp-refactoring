package kitchenpos.menu.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import kitchenpos.menu.application.request.MenuGroupRequest;

public class MenuGroupApiRequest {

    private final String name;

    @JsonCreator
    public MenuGroupApiRequest(String name) {
        this.name = name;
    }

    public MenuGroupRequest toServiceRequest() {
        return new MenuGroupRequest(name);
    }
}
