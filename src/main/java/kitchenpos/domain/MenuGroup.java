package kitchenpos.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MenuGroup {

    @Id
    private Long id;
    private String name;

    public MenuGroup() {
    }

    protected MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MenuGroup{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
