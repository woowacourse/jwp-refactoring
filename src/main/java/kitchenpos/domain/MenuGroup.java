package kitchenpos.domain;

import kitchenpos.config.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;

@AttributeOverride(name = "id", column = @Column(name = "menu_group_id"))
@Entity
public class MenuGroup extends BaseEntity {
    private String name;

    public MenuGroup() {
    }

    public MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroup(String name) {
        this(null, name);
    }

    public String getName() {
        return name;
    }
}
