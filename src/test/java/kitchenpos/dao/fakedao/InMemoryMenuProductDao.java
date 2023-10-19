package kitchenpos.dao.fakedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.menuproduct.MenuProduct;

public class InMemoryMenuProductDao implements MenuProductDao {

    private final List<MenuProduct> menuProducts = new ArrayList<>();

    @Override
    public MenuProduct save(final MenuProduct entity) {
        entity.setSeq((long) (menuProducts.size() + 1));
        menuProducts.add(entity);
        return entity;
    }

    @Override
    public Optional<MenuProduct> findById(final Long id) {
        return menuProducts.stream()
                           .filter(menuProduct -> menuProduct.getSeq().equals(id))
                           .findAny();
    }

    @Override
    public List<MenuProduct> findAll() {
        return menuProducts;
    }

    @Override
    public List<MenuProduct> findAllByMenuId(final Long menuId) {
        return menuProducts.stream()
                           .filter(menuProduct -> menuProduct.getMenuId().equals(menuId))
                           .collect(Collectors.toList());
    }
}
