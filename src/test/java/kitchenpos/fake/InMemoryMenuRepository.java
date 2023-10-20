package kitchenpos.fake;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class InMemoryMenuRepository implements MenuRepository {

    private final Map<Long, Menu> menuDatabase = new HashMap<>();
    private final Map<Long, MenuProduct> menuProductDatabase = new HashMap<>();
    private final AtomicLong menuId = new AtomicLong();
    private final AtomicLong menuProductSeq = new AtomicLong();

    @Override
    public Menu save(Menu entity) {
        if (Objects.isNull(entity.getId())) {
            long id = this.menuId.getAndIncrement();
            Menu menu = new Menu(id, entity.getName(), entity.getPrice(), entity.getMenuGroupId(), saveAll(entity.getMenuProducts().getMenuProducts()));
            menuDatabase.put(id, menu);
            return menu;
        }
        menuDatabase.put(entity.getId(), entity);
        return entity;
    }

    private List<MenuProduct> saveAll(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(this::save)
                .collect(Collectors.toList());
    }

    private MenuProduct save(MenuProduct menuProduct) {
        if (Objects.isNull(menuProduct.getSeq())) {
            long seq = menuProductSeq.getAndIncrement();
            MenuProduct savedMenuProduct = new MenuProduct(seq, menuProduct.getProduct(), menuProduct.getQuantity());
            menuProductDatabase.put(seq, savedMenuProduct);
            return savedMenuProduct;
        }
        menuProductDatabase.put(menuProduct.getSeq(), menuProduct);
        return menuProduct;
    }

    @Override
    public Optional<Menu> findById(Long id) {
        return Optional.ofNullable(menuDatabase.get(id));
    }

    @Override
    public List<Menu> findAll() {
        return new ArrayList<>(menuDatabase.values());
    }

    @Override
    public long countByIdIn(List<Long> ids) {
        return menuDatabase.values().stream()
                .filter(it -> ids.contains(it.getId()))
                .count();
    }
}
