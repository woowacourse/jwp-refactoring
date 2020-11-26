package kitchenpos.ui.dto;

import java.util.Objects;
import javax.validation.constraints.NotEmpty;
import kitchenpos.domain.menugroup.MenuGroup;

public class MenuGroupRequest {

    @NotEmpty
    private String name;

    private MenuGroupRequest() {
    }

    public MenuGroupRequest(String name) {
        this.name = name;
    }

    public MenuGroup toMenuGroup() {
        return MenuGroup.entityOf(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuGroupRequest that = (MenuGroupRequest) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "MenuGroupRequest{" +
            "name='" + name + '\'' +
            '}';
    }
}
