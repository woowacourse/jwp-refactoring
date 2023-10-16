package kitchenpos.fake;

import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.menu.MenuProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeMenuProductDao implements MenuProductDao {

    private Map<Long, MenuProduct> menuProducts = new HashMap<>();
    private Long id = 0L;

    @Override
    public MenuProduct save(MenuProduct entity) {
        if (entity.getSeq() != null) {
            menuProducts.put(entity.getSeq(), entity);
            return entity;
        }
        entity.setSeq(++id);
        menuProducts.put(id, entity);
        return entity;
    }

    @Override
    public Optional<MenuProduct> findById(Long id) {
        return Optional.ofNullable(menuProducts.get(id));
    }

    @Override
    public List<MenuProduct> findAll() {
        return new ArrayList<>(menuProducts.values());
    }

    @Override
    public List<MenuProduct> findAllByMenuId(Long menuId) {
        return menuProducts.values().stream()
                .filter(menuProduct -> menuProduct.getMenuId().equals(menuId))
                .collect(Collectors.toList());
    }
}
