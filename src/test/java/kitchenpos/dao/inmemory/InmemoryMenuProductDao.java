package kitchenpos.dao.inmemory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.MenuProduct;

public class InmemoryMenuProductDao implements MenuProductDao {

    private final Map<Long, MenuProduct> menuProducts;
    private long idValue;

    public InmemoryMenuProductDao() {
        idValue = 0;
        menuProducts = new LinkedHashMap<>();
    }

    @Override
    public MenuProduct save(MenuProduct entity) {
        long savedId = idValue;
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(savedId);
        menuProduct.setMenuId(entity.getMenuId());
        menuProduct.setProductId(entity.getProductId());
        menuProduct.setQuantity(entity.getQuantity());
        this.menuProducts.put(savedId, menuProduct);
        idValue++;
        return menuProduct;
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
                .filter(menuProduct -> Objects.equals(menuProduct.getMenuId(), menuId))
                .collect(Collectors.toList());
    }
}
