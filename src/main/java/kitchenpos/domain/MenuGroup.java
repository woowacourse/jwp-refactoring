package kitchenpos.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@AttributeOverride(name = "id", column = @Column(name = "id"))
@Table(name = "menu_group")
@Entity
public class MenuGroup extends BaseEntity {
    private String name;

    public MenuGroup() {
    }

    public MenuGroup(String name) {
        this.name = name;
    }

    public MenuGroup(Long id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
