package kitchenpos.domain;

public class MenuGroup {
    private Long id;
    private String name;

    private MenuGroup(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroup of(final Long id, final String name) {
        return new MenuGroup(id, name);
    }

    public static MenuGroup from(final String name) {
        return new MenuGroup(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
