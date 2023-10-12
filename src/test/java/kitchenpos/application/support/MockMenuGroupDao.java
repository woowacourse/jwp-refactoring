package kitchenpos.application.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

public class MockMenuGroupDao implements MenuGroupDao {

    private final Map<Long, MenuGroup> store = new HashMap<>();

    @Override
    public MenuGroup save(final MenuGroup entity) {
        entity.setId(1L);
        store.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public Optional<MenuGroup> findById(final Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<MenuGroup> findAll() {
        return store.values().stream().collect(Collectors.toList());
    }

    @Override
    public boolean existsById(final Long id) {
        return store.containsKey(id);
    }
}
