package kitchenpos.domain;

import javax.persistence.Entity;

@Entity
public class MenuGroup extends BaseIdEntity {

    private String name;

    protected MenuGroup() {
    }

    private MenuGroup(Long id, String name) {
        super(id);
        this.name = name;
    }

    public static MenuGroup of(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public static MenuGroup entityOf(String name) {
        return new MenuGroup(null, name);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "MenuGroup{" +
            "id=" + getId() +
            ", name='" + name + '\'' +
            '}';
    }
}
