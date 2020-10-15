package kitchenpos.domain;

import kitchenpos.config.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;

@AttributeOverride(name = "id", column = @Column(name = "menu_group_id"))
@Entity
public class MenuGroup extends BaseEntity {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
