package kitchenpos.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;

public class MenuGroup {

    @Id
    private final Long id;
    private final String name;

    public MenuGroup(final String name) {
        this(null, name);
    }

    @PersistenceCreator
    private MenuGroup(final Long id, final String name) {
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
