package kitchenpos.dao;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import kitchenpos.application.fixture.MenuProductFixtures;
import kitchenpos.domain.MenuProduct;

public class FakeMenuProductDao implements MenuProductDao {

    private static final Map<Long, MenuProduct> STORES = new HashMap<>();
    private static Long id = 0L;

    @Override
    public MenuProduct save(final MenuProduct entity) {
        MenuProduct menuProduct = MenuProductFixtures.generateMenuProduct(++id, entity);
        STORES.put(id, menuProduct);
        return menuProduct;
    }

    @Override
    public Optional<MenuProduct> findById(final Long id) {
        return Optional.of(STORES.get(id));
    }

    @Override
    public List<MenuProduct> findAll() {
        return new ArrayList<>(STORES.values());
    }

    @Override
    public List<MenuProduct> findAllByMenuId(final Long menuId) {
        return STORES.values()
                .stream()
                .filter(menuProduct -> menuProduct.getMenuId() == menuId)
                .collect(toList());
    }

    public static void deleteAll() {
        STORES.clear();
    }
}
