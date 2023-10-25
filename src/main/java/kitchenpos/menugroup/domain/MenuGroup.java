package kitchenpos.menugroup.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MenuGroup {

    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Id
    private Long id;

    @Embedded
    private MenuGroupName name;

    protected MenuGroup() {
    }

    public MenuGroup(final String name) {
        this(null, name);
    }

    public MenuGroup(final Long id, final String name) {
        this.id = id;
        this.name = new MenuGroupName(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public void setName(final String name) {
        this.name = new MenuGroupName(name);
    }
}
