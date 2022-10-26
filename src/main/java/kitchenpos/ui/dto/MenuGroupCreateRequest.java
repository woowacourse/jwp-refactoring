package kitchenpos.ui.dto;

import com.sun.istack.NotNull;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Name;

public class MenuGroupCreateRequest {

    @NotNull
    private String name;

    private MenuGroupCreateRequest() {
    }

    public MenuGroupCreateRequest(final String name) {
        this.name = name;
    }

    public MenuGroup toMenuGroup() {
        return new MenuGroup(new Name(name));
    }

    public String getName() {
        return name;
    }
}
