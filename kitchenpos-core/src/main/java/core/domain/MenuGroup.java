package core.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;

public class MenuGroup {
    @Id
    private Long id;
    private String name;

    public MenuGroup(String name) {
        this(null, name);
    }

    @PersistenceCreator
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
