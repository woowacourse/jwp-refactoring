package kitchenpos.menu.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class MenuName {

    private String name;

    protected MenuName() {
    }

    public MenuName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuName menuName = (MenuName) o;
        return Objects.equals(getName(), menuName.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
