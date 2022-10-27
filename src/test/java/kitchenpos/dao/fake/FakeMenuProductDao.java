package kitchenpos.dao.fake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.MenuProduct;

public class FakeMenuProductDao implements MenuProductDao {

    private long id = 0L;
    private final Map<Long, MenuProduct> menuProducts = new HashMap<>();

    @Override
    public MenuProduct save(final MenuProduct entity) {
        long savedId = ++id;
        menuProducts.put(savedId, entity);
        entity.setSeq(savedId);
        return entity;
    }

    @Override
    public Optional<MenuProduct> findById(final Long id) {
        return Optional.ofNullable(menuProducts.get(id));
    }

    @Override
    public List<MenuProduct> findAll() {
        return List.copyOf(menuProducts.values());
    }

    @Override
    public List<MenuProduct> findAllByMenuId(final Long menuId) {
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (MenuProduct menuProduct : menuProducts.values()) {
            if (menuProduct.getMenuId().equals(menuId)) {
                savedMenuProducts.add(menuProduct);
            }
        }
        return savedMenuProducts;
    }
}
