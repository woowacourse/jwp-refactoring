package kitchenpos.domain;

import lombok.Getter;

@Getter
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

    public void setId(final Long id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
