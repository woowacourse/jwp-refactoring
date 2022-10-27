package kitchenpos.dao.fake;

import static kitchenpos.application.fixture.MenuGroupFixtures.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

public class FakeMenuGroupDao implements MenuGroupDao {

    private static final Map<Long, MenuGroup> stores = new HashMap<>();
    private static Long id = 0L;

    @Override
    public MenuGroup save(final MenuGroup entity) {
        MenuGroup menuGroup = generateMenuGroup(++id, entity);
        stores.put(id, menuGroup);
        return menuGroup;
    }

    @Override
    public Optional<MenuGroup> findById(final Long id) {
        return Optional.of(stores.get(id));
    }

    @Override
    public List<MenuGroup> findAll() {
        return new ArrayList<>(stores.values());
    }

    @Override
    public boolean existsById(final Long id) {
        return stores.containsKey(id);
    }

    public static void deleteAll() {
        stores.clear();
    }
}
