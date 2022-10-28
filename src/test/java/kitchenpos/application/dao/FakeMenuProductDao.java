package kitchenpos.application.dao;

import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.MenuProduct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeMenuProductDao implements MenuProductDao {

    private final Map<Long, MenuProduct> menuProducts = new HashMap<>();

    private long id = 1;

    @Override
    public MenuProduct save(final MenuProduct menuProduct) {
        menuProduct.setSeq(id);
        menuProducts.put(id++, menuProduct);
        return menuProduct;    }

    @Override
    public Optional<MenuProduct> findById(final Long id) {
        return Optional.ofNullable(menuProducts.get(id));
    }

    @Override
    public List<MenuProduct> findAll() {
        return menuProducts.values()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<MenuProduct> findAllByMenuId(final Long menuId) {
        return menuProducts.values()
                .stream()
                .filter(menuProduct -> menuProduct.getMenuId().equals(menuId))
                .collect(Collectors.toUnmodifiableList());
    }

    public void clear() {
        menuProducts.clear();
    }
}
