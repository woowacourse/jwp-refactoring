package kitchenpos.menugroup.domain;

import java.util.Objects;
import kitchenpos.product.domain.Name;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;

public class MenuGroup {
    @Id
    private Long id;
    @Embedded.Empty
    private Name name;

    public MenuGroup(final Long id, final Name name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroup(final Name name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
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
        return Objects.equals(id, menuGroup.id) && Objects.equals(name, menuGroup.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
