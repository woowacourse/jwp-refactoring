package kitchenpos.domain.Menu;

import java.util.Objects;

public class MenuGroup {

    private Long id;
    private MenuGroupName name;

    public MenuGroup(MenuGroupName name) {
        this(null, name);
    }

    public MenuGroup(Long id, MenuGroupName name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuGroup menuGroup = (MenuGroup) o;
        return Objects.equals(getId(), menuGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
