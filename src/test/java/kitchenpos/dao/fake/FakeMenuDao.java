package kitchenpos.dao.fake;

import static kitchenpos.application.fixture.MenuFixtures.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;

public class FakeMenuDao implements MenuDao {

    private static final Map<Long, Menu> stores = new HashMap<>();
    private static Long id = 0L;

    @Override
    public Menu save(final Menu entity) {
        Menu menu = generateMenu(++id, entity);
        stores.put(id, menu);
        return menu;
    }

    @Override
    public Optional<Menu> findById(final Long id) {
        return Optional.of(stores.get(id));
    }

    @Override
    public List<Menu> findAll() {
        return new ArrayList<>(stores.values());
    }

    @Override
    public long countByIdIn(final List<Long> ids) {
        return stores.keySet()
                .stream()
                .filter(ids::contains)
                .count();
    }

    public static void deleteAll() {
        stores.clear();
    }
}
