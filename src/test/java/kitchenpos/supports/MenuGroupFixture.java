package kitchenpos.supports;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    private Long id = null;
    private String name = "안주류";

    private MenuGroupFixture() {
    }

    public static MenuGroupFixture fixture() {
        return new MenuGroupFixture();
    }

    public MenuGroupFixture id(Long id) {
        this.id = id;
        return this;
    }

    public MenuGroupFixture name(String name) {
        this.name = name;
        return this;
    }

    public MenuGroup build() {
        return new MenuGroup(id, name);
    }
}
