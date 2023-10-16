package kitchenpos.application.fakedao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;

public class InMemoryMenuDao implements MenuDao {

    private final List<Menu> menus = new ArrayList<>();

    @Override
    public Menu save(final Menu entity) {
        entity.setId((long) (menus.size() + 1));
        menus.add(entity);
        return entity;
    }

    @Override
    public Optional<Menu> findById(final Long id) {
        return menus.stream()
                    .filter(menu -> menu.getId().equals(id))
                    .findAny();
    }

    @Override
    public List<Menu> findAll() {
        return menus;
    }

    @Override
    public long countByIdIn(final List<Long> ids) {
        return menus.stream()
                    .filter(menu -> ids.contains(menu.getId()))
                    .count();
    }
}
