package kitchenpos.domain;

import java.util.Objects;

/**
 * 메뉴 묶음, 분류
 */
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
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final MenuGroup menuGroup = (MenuGroup)o;
        return Objects.equals(id, menuGroup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
