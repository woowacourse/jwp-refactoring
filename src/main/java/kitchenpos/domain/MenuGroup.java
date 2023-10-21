package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    protected MenuGroup(){
    }

    private MenuGroup(String name) {
        this(null, name);
    }

    private MenuGroup(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroup from(String name) {
        return new MenuGroup(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
