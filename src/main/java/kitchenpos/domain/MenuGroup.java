package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class MenuGroup extends BaseEntity {

    @Column(nullable = false)
    private String name;

    public MenuGroup(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    protected MenuGroup() {
    }
}
