package kitchenpos.application.dao;

import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FakeMenuDao implements MenuDao {

    private final Map<Long, Menu> menus = new HashMap<>();

    private long id = 1;

    @Override
    public Menu save(final Menu menu) {
        menu.setId(id);
        menus.put(id++, menu);
        return menu;
    }

    @Override
    public Optional<Menu> findById(final Long id) {
        return Optional.ofNullable(menus.get(id));
    }

    @Override
    public List<Menu> findAll() {
        return menus.values()
                .stream()
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public long countByIdIn(final List<Long> ids) {
        return menus.keySet()
                .stream()
                .filter(ids::contains)
                .count();
    }

    public void clear() {
        menus.clear();
    }
}
