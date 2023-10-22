package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany
    @JoinColumn(name = "menu_group_id")
    private List<Menu> menus;

    protected MenuGroup() {
    }

    public MenuGroup(final Long id, final String name, final List<Menu> menus) {
        this.id = id;
        this.name = name;
        this.menus = menus;
    }

    public static MenuGroup forSave(final String name) {
        return new MenuGroup(null, name, new ArrayList<>());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Menu> getMenus() {
        return new ArrayList<>(menus);
    }
}
