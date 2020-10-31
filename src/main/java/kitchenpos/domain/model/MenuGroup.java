package kitchenpos.domain.model;

public class MenuGroup {
    private final Long id;
    private final String name;

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
