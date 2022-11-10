package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.domain.MenuProduct;

public class FakeMenuProductDao implements MenuProductDao {

    private final List<MenuProduct> IN_MEMORY_MENU_PRODUCT;
    private Long seq;

    public FakeMenuProductDao() {
        IN_MEMORY_MENU_PRODUCT = new ArrayList<>();
        seq = 1L;
    }

    @Override
    public MenuProduct save(MenuProduct entity) {
        MenuProduct menuProduct = new MenuProduct(seq++, entity.getProductId(), entity.getMenuId(),
                entity.getQuantity());
        IN_MEMORY_MENU_PRODUCT.add(menuProduct);
        return menuProduct;
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
