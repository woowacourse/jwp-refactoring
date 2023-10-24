package kitchenpos.dao.fakedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.menuproduct.MenuProduct;

public class InMemoryMenuProductDao implements MenuProductDao {

    private final List<MenuProduct> menuProducts = new ArrayList<>();

    @Override
    public MenuProduct save(final MenuProduct entity) {
        final var id = (long) (menuProducts.size() + 1);
        final var saved = new MenuProduct(id, entity.getProduct(), entity.getQuantity());
        menuProducts.add(saved);
        return saved;
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
}
