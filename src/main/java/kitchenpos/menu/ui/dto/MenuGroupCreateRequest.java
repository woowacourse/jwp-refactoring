package kitchenpos.menu.ui.dto;

import com.sun.istack.NotNull;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.Name;

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
