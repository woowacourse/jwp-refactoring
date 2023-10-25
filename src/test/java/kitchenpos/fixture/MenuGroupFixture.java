package kitchenpos.fixture;

import java.util.function.Consumer;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;

public enum MenuGroupFixture {

    LUNCH(1L, "Lunch Specials");

    private final Long id;
    private final String name;

    MenuGroupFixture(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MenuGroup computeDefaultMenu(Consumer<MenuGroup> consumer) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("Lunch Specials");
        return menuGroup;
    }

    public MenuGroup toEntity() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }
}