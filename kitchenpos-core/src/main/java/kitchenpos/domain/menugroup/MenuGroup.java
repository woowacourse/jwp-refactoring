package kitchenpos.domain.menugroup;

import javax.persistence.*;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    protected MenuGroup() {
    }

    public MenuGroup(final Name name) {
        this(null, name);
    }

    public MenuGroup(final Long id, final Name name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroup create(final String name) {
        return new MenuGroup(Name.create(name));
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }
}
