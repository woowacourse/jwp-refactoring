package kitchenpos.domain;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class MenuGroup {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String name;

    protected MenuGroup() {
    }

    public MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
