package kitchenpos.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;

public class FakeMenuDao implements MenuDao {

    private final List<Menu> IN_MEMORY_MENU;
    private Long id;

    public FakeMenuDao() {
        IN_MEMORY_MENU = new ArrayList<>();
        id = 1L;
    }

    @Override
    public Menu save(Menu entity) {
        Menu menu = new Menu(id++, entity.getName(), entity.getPrice(), entity.getMenuGroupId(),
                entity.getMenuProducts());
        IN_MEMORY_MENU.add(menu);
        return menu;
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
