package kitchenpos.factory;

import org.springframework.stereotype.Component;

import kitchenpos.domain.MenuGroup;

@Component
public class MenuGroupFactory {
    public MenuGroup create(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }

    public MenuGroup create(String name) {
        return create(null, name);
    }
}
