package kitchenpos.domain.menu;

public class MenuGroup {

    private final Long id;
    private final String name;

    public MenuGroup(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroup ofNew(final String name) {
        return new MenuGroup(null, name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
