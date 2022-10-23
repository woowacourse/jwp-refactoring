package kitchenpos.domain.menu;

public class MenuGroup {

    private Long id;
    private String name;

    public MenuGroup(Long id, String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException();
        }
        this.id = id;
        this.name = name;
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
