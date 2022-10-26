package kitchenpos.dao;

import static kitchenpos.application.fixture.MenuGroupFixtures.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kitchenpos.domain.MenuGroup;

public class FakeMenuGroupDao implements MenuGroupDao {

    private static final Map<Long, MenuGroup> STORES = new HashMap<>();
    private static Long id = 0L;

    @Override
    public MenuGroup save(final MenuGroup entity) {
        MenuGroup menuGroup = generateMenuGroup(++id, entity);
        STORES.put(id, menuGroup);
        return menuGroup;
    }

    @Override
    public Optional<MenuGroup> findById(final Long id) {
        return Optional.of(STORES.get(id));
    }

    @Override
    public List<MenuGroup> findAll() {
        return new ArrayList<>(STORES.values());
    }

    @Override
    public boolean existsById(final Long id) {
        return STORES.containsKey(id);
    }

    public static void deleteAll() {
        STORES.clear();
    }
}
