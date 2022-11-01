package kitchenpos.domain.menu;

public class MenuGroup {

    private final Long id;
    private final String name;

    /**
     * DB 에 저장되지 않은 객체
     */
    public MenuGroup(final String name) {
        this(null, name);
    }

    /**
     * DB 에 저장된 객체
     */
    public MenuGroup(final Long id, final String name) {
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
