package kitchenpos.generator;

import kitchenpos.domain.MenuGroup;

public class MenuGroupGenerator {

    public static MenuGroup newInstance(String name) {
        return newInstance(null, name);
    }

    public static MenuGroup newInstance(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);

        return menuGroup;
    }
}
