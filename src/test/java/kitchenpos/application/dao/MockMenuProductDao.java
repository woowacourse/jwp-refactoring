package kitchenpos.application.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.MenuProduct;

public class MockMenuProductDao implements MenuProductDao {

    private Map<Long, MenuProduct> menuProducts = new HashMap<>();
    private Long id = 1L;

    @Override
    public MenuProduct save(MenuProduct entity) {
        if (Objects.isNull(entity.getSeq())) {
            entity.setSeq(id++);
        }
        menuProducts.put(entity.getSeq(), entity);
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
