package kitchenpos.menugroup.domain;

import kitchenpos.common.domain.Name;

public class MenuGroup {
    private Long id;
    private Name name;

    public MenuGroup(String name) {
        this(null, name);
    }

    public MenuGroup(Long id, String name) {
        this.id = id;
        this.name = new Name(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }
}
