package kitchenpos.menu.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    protected MenuGroup() {}

    public static MenuGroup create(Long id, String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.id = id;
        menuGroup.name = name;
        return menuGroup;
    }

    public static MenuGroup create(String name) {
        return create(null, name);
    }

    public static MenuGroup create(Long id) {
        return create(id, null);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
