package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;

public class FakeMenuDao implements MenuDao {

    private final List<Menu> IN_MEMORY_MENU;

    public FakeMenuDao() {
        IN_MEMORY_MENU = new ArrayList<>();
    }

    @Override
    public Menu save(Menu entity) {
        IN_MEMORY_MENU.add(entity);
        Long id = (long) IN_MEMORY_MENU.size();
        entity.setId(id);
        return entity;
    }

    @Override
    public Optional<Menu> findById(Long id) {
        return IN_MEMORY_MENU.stream()
                .filter(menu -> menu.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Menu> findAll() {
        return new ArrayList<>(IN_MEMORY_MENU);
    }

    @Override
    public long countByIdIn(List<Long> ids) {
        return IN_MEMORY_MENU.stream()
                .filter(menu -> ids.contains(menu.getId()))
                .count();
    }
}
