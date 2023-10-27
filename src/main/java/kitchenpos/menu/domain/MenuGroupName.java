package kitchenpos.menu.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class MenuGroupName {

    private String name;

    protected MenuGroupName() {
    }

    public MenuGroupName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuGroupName that = (MenuGroupName) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
