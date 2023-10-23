package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
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

    @OneToMany(mappedBy = "menuGroup", cascade = CascadeType.ALL)
    private List<Menu> menus = new ArrayList<>();

    protected MenuGroup() {
    }

    public MenuGroup(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroup forSave(final String name) {
        return new MenuGroup(null, name);
    }

    public void addMenu(final Menu menu) {
        menus.add(menu);
        menu.joinMenuGroup(this);
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
