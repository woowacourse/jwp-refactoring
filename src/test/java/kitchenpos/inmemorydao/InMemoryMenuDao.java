package kitchenpos.inmemorydao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;

public class InMemoryMenuDao implements MenuDao {
    private Map<Long, Menu> menus;
    private long index;

    public InMemoryMenuDao() {
        this.menus = new HashMap<>();
        this.index = 0L;
    }

    @Override
    public Menu save(final Menu entity) {
        Long key = entity.getId();

        if (key == null) {
            key = index++;
        }

        final Menu menu = new Menu();
        menu.setId(key);
        menu.setName(entity.getName());
        menu.setPrice(entity.getPrice());
        menu.setMenuGroupId(entity.getMenuGroupId());
        menu.setMenuProducts(entity.getMenuProducts());

        menus.put(key, menu);
        return menu;
    }

    @Override
    public Optional<Menu> findById(final Long id) {
        return Optional.of(menus.get(id));
    }

    @Override
    public List<Menu> findAll() {
        return new ArrayList<>(menus.values());
    }

    @Override
    public long countByIdIn(final List<Long> ids) {
        return ids.stream()
                .map(menus::get)
                .filter(Objects::nonNull)
                .count()
                ;
    }
}
