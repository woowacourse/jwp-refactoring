package kitchenpos.domain;

import java.util.Objects;

public class MenuGroup {
    private Long id;
    private String name;

    public MenuGroup() {
    }

    public MenuGroup(final String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuGroup menuGroup = (MenuGroup) o;
        if (id == null || menuGroup.id == null) {
            return Objects.equals(name, menuGroup.name);
        }
        return Objects.equals(id, menuGroup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
