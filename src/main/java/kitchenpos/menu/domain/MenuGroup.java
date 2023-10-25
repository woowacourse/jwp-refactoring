package kitchenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.menu.domain.vo.MenuGroupName;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MenuGroupName menuGroupName;

    protected MenuGroup() {
    }

    public MenuGroup(String name) {
        this.menuGroupName = new MenuGroupName(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return menuGroupName.getName();
    }
}
