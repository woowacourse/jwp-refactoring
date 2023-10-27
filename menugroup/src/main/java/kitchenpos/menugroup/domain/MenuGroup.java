package kitchenpos.menugroup.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuGroup {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    public MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroup(String name) {
        this(null, name);
    }

    protected MenuGroup() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
