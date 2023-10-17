package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "menu_group")
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_group_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "menuGroup")
    private List<Menu> menus = new ArrayList<>();

    protected MenuGroup() {
    }

    private MenuGroup(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static MenuGroupBuilder builder() {
        return new MenuGroupBuilder();
    }

    public static class MenuGroupBuilder {

        private String name;

        public MenuGroupBuilder name(String name) {
            this.name = name;
            return this;
        }

        public MenuGroup build() {
            return new MenuGroup(name);
        }
    }
}
