package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.domain.MenuGroup;

public class FakeMenuGroupDao implements MenuGroupDao {

    private final List<MenuGroup> IN_MEMORY_MENU_GROUP;
    private Long id;

    public FakeMenuGroupDao() {
        this.IN_MEMORY_MENU_GROUP = new ArrayList<>();
        this.id = 1L;
    }

    @Override
    public MenuGroup save(MenuGroup entity) {
        MenuGroup menuGroup = new MenuGroup(id++, entity.getName());
        IN_MEMORY_MENU_GROUP.add(menuGroup);
        return menuGroup;
    }

    @Override
    public Optional<MenuGroup> findById(Long id) {
        return IN_MEMORY_MENU_GROUP.stream()
                .filter(menuGroup -> menuGroup.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<MenuGroup> findAll() {
        return new ArrayList<>(IN_MEMORY_MENU_GROUP);
    }

    @Override
    public boolean existsById(Long id) {
        Optional<MenuGroup> findMenuGroup = IN_MEMORY_MENU_GROUP.stream()
                .filter(menuGroup -> menuGroup.getId().equals(id))
                .findFirst();
        return findMenuGroup.isPresent();
    }
}
