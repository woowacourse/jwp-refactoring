package kitchenpos.domain;

public class MenuGroup {
    private final Long id;
    private final String name;

    public MenuGroup(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public MenuGroup(final String name) {
        this(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
