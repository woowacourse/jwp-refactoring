package kitchenpos.domain;

import javax.persistence.*;

@Entity
public class MenuGroup {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    public MenuGroup() {
    }

    public MenuGroup(String name) {
        this(null, name);
    }

    public MenuGroup(Long id, String name) {
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
}
