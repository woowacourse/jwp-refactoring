package kitchenpos.menugroup.domain;

import javax.persistence.*;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;

    protected MenuGroup() {
    }

    private MenuGroup(String name) {
        this(null, name);
    }

    private MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroup create(String name) {
        return new MenuGroup(name);
    }

    public static MenuGroup create(Long id, String name) {
        return new MenuGroup(id, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
