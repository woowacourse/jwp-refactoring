package kitchenpos.menu.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MenuGroup {

    @GeneratedValue
    @Id
    private Long id;
    private String name;

    public MenuGroup() {
    }

    public MenuGroup(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
