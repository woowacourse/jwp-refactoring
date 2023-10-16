package kitchenpos.fake;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.menu.MenuGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeMenuGroupDao implements MenuGroupDao {

    private Map<Long, MenuGroup> menuGroups = new HashMap<>();
    private Long id = 0L;

    @Override
    public MenuGroup save(MenuGroup entity) {
        if (entity.getId() != null) {
            menuGroups.put(entity.getId(), entity);
            return entity;
        }
        entity.setId(++id);
        menuGroups.put(id, entity);
        return entity;
    }

    @Override
    public Optional<MenuGroup> findById(Long id) {
        return Optional.ofNullable(menuGroups.get(id));
    }

    @Override
    public List<MenuGroup> findAll() {
        return new ArrayList<>(menuGroups.values());
    }

    @Override
    public boolean existsById(Long id) {
        return menuGroups.containsKey(id);
    }
}
