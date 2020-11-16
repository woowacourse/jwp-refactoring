package kitchenpos.inmemorydao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.MenuProduct;

public class InMemoryMenuProductDao implements MenuProductDao {
    private Map<Long, MenuProduct> menuProducts;
    private long index;

    public InMemoryMenuProductDao() {
        this.menuProducts = new HashMap<>();
        this.index = 0;
    }

    @Override
    public MenuProduct save(final MenuProduct entity) {
        Long seq = entity.getSeq();

        if (seq == null) {
            seq = index++;
        }

        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setSeq(seq);
        menuProduct.setMenuId(entity.getMenuId());
        menuProduct.setProductId(entity.getProductId());
        menuProduct.setQuantity(entity.getQuantity());

        menuProducts.put(seq, menuProduct);
        return menuProduct;
    }

    @Override
    public Optional<MenuProduct> findById(final Long id) {
        return Optional.of(menuProducts.get(id));
    }

    @Override
    public List<MenuProduct> findAll() {
        return new ArrayList<>(menuProducts.values());
    }

    @Override
    public List<MenuProduct> findAllByMenuId(final Long menuId) {
        return menuProducts.values()
                .stream()
                .filter(menuProduct -> menuId.equals(menuProduct.getMenuId()))
                .collect(Collectors.toList())
                ;
    }
}
