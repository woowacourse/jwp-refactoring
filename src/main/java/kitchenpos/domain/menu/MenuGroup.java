package kitchenpos.domain.menu;

import kitchenpos.domain.common.Name;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Embedded
    private Name name;

    private MenuGroup(final Long id, final Name name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroup of(final String name) {
        return new MenuGroup(null, Name.of(name));
    }

    protected MenuGroup() {
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }
}
