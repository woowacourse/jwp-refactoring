package kitchenpos.domain.menugroup;

import java.util.Objects;

public class MenuGroup {
    private Long id;
    private MenuGroupName name;

    public MenuGroup(final MenuGroupName name) {
        this(null, name);
    }

    public MenuGroup(final Long id, final MenuGroupName name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
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
        return Objects.equals(id, menuGroup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
