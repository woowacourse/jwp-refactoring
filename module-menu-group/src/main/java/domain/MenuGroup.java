package domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    private final MenuGroupName menuGroupName;

    public MenuGroup() {
        this.id = null;
        this.menuGroupName = null;
    }

    public MenuGroup(final MenuGroupName menuGroupName) {
        this.id = null;
        this.menuGroupName = menuGroupName;
    }

    public Long getId() {
        return id;
    }

    public MenuGroupName getMenuGroupName() {
        return menuGroupName;
    }
}
