package kitchenpos.domain;

import javax.persistence.Entity;

@Entity
public class MenuGroup extends BaseEntity {

    private String name;

    public MenuGroup() {}

    public MenuGroup(Long id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
