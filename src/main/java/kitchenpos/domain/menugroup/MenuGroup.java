package kitchenpos.domain.menugroup;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import kitchenpos.domain.base.BaseIdEntity;

@Entity
public class MenuGroup extends BaseIdEntity {

    @Embedded
    private MenuGroupName name;

    protected MenuGroup() {
    }

    private MenuGroup(Long id, MenuGroupName name) {
        super(id);
        this.name = name;
    }

    public static MenuGroup of(Long id, String name) {
        return new MenuGroup(id, new MenuGroupName(name));
    }

    public static MenuGroup entityOf(String name) {
        return of(null, name);
    }

    public String getName() {
        return name.getName();
    }

    @Override
    public String toString() {
        return "MenuGroup{" +
            "id=" + getId() +
            ", name='" + name + '\'' +
            '}';
    }
}
