package kitchenpos.domain;


import io.micrometer.core.instrument.util.StringUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    protected MenuGroup() {
    }

    public MenuGroup(Long id, String name) {
        validateMenuGroupName(name);
        this.id = id;
        this.name = name;
    }

    private void validateMenuGroupName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException();
        }
    }

    public MenuGroup(String name) {
        this(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
