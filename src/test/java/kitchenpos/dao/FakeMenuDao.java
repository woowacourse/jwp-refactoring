package kitchenpos.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kitchenpos.application.fixture.MenuFixtures;
import kitchenpos.domain.Menu;

public class FakeMenuDao implements MenuDao {

    private static final Map<Long, Menu> STORES = new HashMap<>();
    private static Long id = 0L;

    @Override
    public Menu save(final Menu entity) {
        Menu menu = MenuFixtures.generateMenu(++id, entity);
        STORES.put(id, menu);
        return menu;
    }

    @Override
    public Optional<Menu> findById(final Long id) {
        return Optional.of(STORES.get(id));
    }

    @Override
    public List<Menu> findAll() {
        return new ArrayList<>(STORES.values());
    }

    @Override
    public long countByIdIn(final List<Long> ids) {
        return STORES.keySet()
                .stream()
                .filter(ids::contains)
                .count();
    }

    public static void deleteAll() {
        STORES.clear();
    }
}
