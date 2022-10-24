package kitchenpos.domain;

import java.util.Objects;

public class MenuGroup {
    private Long id;
    private String name;

    public MenuGroup() {
    }

    public MenuGroup(String name) {
        this(null, name);
    }

    public MenuGroup(Long id, String name) {
        this.id = id;
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MenuGroup)) {
            return false;
        }
        MenuGroup menuGroup = (MenuGroup) o;
        return Objects.equals(getName(), menuGroup.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
