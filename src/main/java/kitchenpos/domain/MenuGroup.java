package kitchenpos.domain;

import java.util.Objects;

public class MenuGroup {

    private Long id;
    private String name;

    private MenuGroup() {
    }

    public MenuGroup(Long id, String name) {
        validateName(name);
        this.id = id;
        this.name = name;
    }

    public MenuGroup(String name) {
        this(null, name);
    }

    private void validateName(String name) {
        if (Objects.isNull(name) || name.isEmpty()) {
            throw new IllegalArgumentException("menu group name cannot be empty.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
