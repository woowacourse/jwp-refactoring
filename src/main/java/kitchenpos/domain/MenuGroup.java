package kitchenpos.domain;

import java.util.Objects;

public class MenuGroup {

    private Long id;
    private String name;

    public MenuGroup(final String name) {
        this(null, name);
    }

    public MenuGroup(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MenuGroup menuGroup = (MenuGroup) o;
        if (Objects.isNull(this.id) || Objects.isNull(menuGroup.id)) {
            return false;
        }
        return Objects.equals(id, menuGroup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
