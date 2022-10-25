package kitchenpos.domain;

import java.util.Objects;

public class MenuGroup {
    private Long id;
    private String name;

    public MenuGroup(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroup(final String name) {
        this(null, name);
    }

    private MenuGroup() {
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
        if (!(o instanceof MenuGroup menuGroup)) {
            return false;
        }
        return Objects.equals(id, menuGroup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
