package kitchenpos.domain;

public class MenuGroup {
    private Long id;
    private String name;

    public MenuGroup(final String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void updateId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
}
