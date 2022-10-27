package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuProduct;

public class FakeMenuProductDao implements MenuProductDao {

    private final List<MenuProduct> IN_MEMORY_MENU_PRODUCT;

    public FakeMenuProductDao() {
        IN_MEMORY_MENU_PRODUCT = new ArrayList<>();
    }

    @Override
    public MenuProduct save(MenuProduct entity) {
        IN_MEMORY_MENU_PRODUCT.add(entity);
        Long seq = (long) IN_MEMORY_MENU_PRODUCT.size();
        entity.setSeq(seq);
        return entity;
    }

    @Override
    public Optional<MenuProduct> findById(Long seq) {
        return IN_MEMORY_MENU_PRODUCT.stream()
                .filter(menu -> menu.getSeq().equals(seq))
                .findFirst();
    }

    @Override
    public List<MenuProduct> findAll() {
        return new ArrayList<>(IN_MEMORY_MENU_PRODUCT);
    }

    @Override
    public List<MenuProduct> findAllByMenuId(Long menuId) {
        return IN_MEMORY_MENU_PRODUCT.stream()
                .filter(menuProduct -> menuProduct.getMenuId().equals(menuId))
                .collect(Collectors.toList());
    }
}
