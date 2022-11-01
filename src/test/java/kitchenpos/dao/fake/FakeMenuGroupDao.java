package kitchenpos.dao.fake;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

public class FakeMenuGroupDao implements MenuGroupDao {

    private long id = 0L;
    private final Map<Long, MenuGroup> menuGroups = new HashMap<>();

    @Override
    public MenuGroup save(final MenuGroup entity) {
        long savedId = ++id;
        menuGroups.put(savedId, entity);
        entity.setId(savedId);
        return entity;
    }

    @Override
    public Optional<MenuGroup> findById(final Long id) {
        return Optional.ofNullable(menuGroups.get(id));
    }

    @Override
    public List<MenuGroup> findAll() {
        return List.copyOf(menuGroups.values());
    }

    @Override
    public boolean existsById(final Long id) {
        return menuGroups.containsKey(id);
    }
}
