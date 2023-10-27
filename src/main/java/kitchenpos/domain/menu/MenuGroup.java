package kitchenpos.domain.menu;

import kitchenpos.common.BaseDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class MenuGroup extends BaseDate {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;

    public MenuGroup(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroup(final String name) {
        this(null, name);
    }

    protected MenuGroup() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "MenuGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
