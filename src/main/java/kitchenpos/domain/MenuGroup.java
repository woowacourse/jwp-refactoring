package kitchenpos.domain;

public class MenuGroup {

    private Long id;
    private String name;

    private MenuGroup() {
    }

    public MenuGroup(Long id, String name) {
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
